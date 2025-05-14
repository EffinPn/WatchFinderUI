package com.example.watchfinder.data.dto

data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)