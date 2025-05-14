package com.example.watchfinder.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchfinder.data.UiState.ForgotPasswordUiState
import com.example.watchfinder.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordVM @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun resetState() {
        
        _uiState.value = ForgotPasswordUiState()
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(
            email = email,
            error = null
        ) }
    }

    fun sendResetPasswordEmail() {
        if (_uiState.value.isLoading) return 

        val email = _uiState.value.email.trim()

        
        if (email.isBlank()) {
            _uiState.update { it.copy(error = "El email no puede estar vacío") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                authRepository.sendPasswordResetEmail(email)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEmailSent = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error al enviar el email"
                    )
                }
            }
        }
    }
}