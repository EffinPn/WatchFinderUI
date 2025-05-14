package com.example.watchfinder.data.dto

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)