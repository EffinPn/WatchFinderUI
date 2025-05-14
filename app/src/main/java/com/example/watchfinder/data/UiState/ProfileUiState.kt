package com.example.watchfinder.data.UiState

import com.example.watchfinder.data.model.User

data class ProfileUiState(
    val user: User? = null,
    val newEmail: String = "",
    val newUserName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val fullProfileImageUrl: String? = null,
    val isEmailUpdateSuccess: Boolean = false,
    val isUserNameUpdateSuccess: Boolean = false,
    val isImageUpdateSuccess: Boolean = false,
    val emailEdited: Boolean = false,
    val usernameEdited: Boolean = false
)