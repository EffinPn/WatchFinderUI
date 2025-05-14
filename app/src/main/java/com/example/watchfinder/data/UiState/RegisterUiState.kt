package com.example.watchfinder.data.UiState


data class RegisterUiState(
    val nameInput: String = "",
    val nickInput: String = "",
    val passwordInput: String = "",
    val repeatPasswordInput: String = "",
    val birthDateInput: String = "",
    val emailInput: String = "",
    val isLoading: Boolean = false,
    val registrationError: String? = null,
    val registrationSuccess: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isRepeatPasswordVisible: Boolean = false
)