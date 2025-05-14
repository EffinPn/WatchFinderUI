package com.example.watchfinder.viewmodels

import LoginUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchfinder.data.dto.LoginResponse
import com.example.watchfinder.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val authRepository: AuthRepository, 
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(usernameInput = username, loginError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(passwordInput = password, loginError = null) }
    }

    fun attemptLogin() {
        if (_uiState.value.isLoading) return 

        val username = _uiState.value.usernameInput.trim()
        val password = _uiState.value.passwordInput

        
        if (username.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(loginError = "Usuario y contraseña no pueden estar vacíos") }
            return
        }

        _uiState.update { it.copy(isLoading = true, loginError = null, loginSuccess = false) }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null, loginSuccess = false) }

            
            val loginResult: Result<LoginResponse> = authRepository.login(username, password)

            
            if (loginResult.isSuccess) {
                
                
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            } else {
                
                val exception = loginResult.exceptionOrNull() ?: Exception("Error desconocido en login")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginError = "Fallo en login: ${exception.message ?: "Inténtalo de nuevo"}"
                    )
                }
            }
        }
    }

    
    fun onLoginNavigated() {
        _uiState.update { it.copy(loginSuccess = false) }
    }
}