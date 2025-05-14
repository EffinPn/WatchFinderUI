package com.example.watchfinder.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchfinder.data.UiState.RegisterUiState
import com.example.watchfinder.repository.AuthRepository 
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class RegisterVM @Inject constructor(
    private val authRepository: AuthRepository 
    
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    
    fun onNameChange(name: String) {
        _uiState.update { it.copy(nameInput = name, registrationError = null) }
    }

    fun onNickChange(nick: String) {
        _uiState.update { it.copy(nickInput = nick, registrationError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(passwordInput = password, registrationError = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(emailInput = email, registrationError = null) }
    }

    fun onRepeatPasswordChange(repeatPassword: String) {
        _uiState.update { it.copy(repeatPasswordInput = repeatPassword, registrationError = null) }
    }

    fun onBirthDateChange(birthDate: String) {
        _uiState.update { it.copy(birthDateInput = birthDate, registrationError = null) }
    }

    
    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleRepeatPasswordVisibility() {
        _uiState.update { it.copy(isRepeatPasswordVisible = !it.isRepeatPasswordVisible) }
    }


    
    fun attemptRegistration() {
        if (_uiState.value.isLoading) return

        val name = _uiState.value.nameInput.trim()
        val nick = _uiState.value.nickInput.trim()
        val password = _uiState.value.passwordInput 
        val repeatPassword = _uiState.value.repeatPasswordInput
        val email = _uiState.value.emailInput


        if (name.isBlank() || nick.isBlank() || password.isBlank() || repeatPassword.isBlank()) {
            _uiState.update { it.copy(registrationError = "Todos los campos son obligatorios") }
            return
        }
        if (password != repeatPassword) {
            _uiState.update { it.copy(registrationError = "Las contraseñas no coinciden") }
            return
        }
        if (email.isBlank()) {
            _uiState.update { it.copy(registrationError = "Debes seleccionar tu email") }
            return
        }
        

        
        _uiState.update { it.copy(isLoading = true, registrationError = null, registrationSuccess = false) }

        viewModelScope.launch {
            
            
            
            val registerResult = authRepository.register(
                name = name,
                username = nick,
                password = password,
                email = email
            )

            if (registerResult.isSuccess) {
                
                _uiState.update { it.copy(isLoading = false, registrationSuccess = true) }
                
            } else {
                
                val errorMsg = registerResult.exceptionOrNull()?.message ?: "Error desconocido"
                _uiState.update { it.copy(isLoading = false, registrationError = "Error en registro: $errorMsg") }
            }
        }
    }

    
    fun onRegistrationNavigated() {
        _uiState.update { it.copy(registrationSuccess = false) }
    }
}