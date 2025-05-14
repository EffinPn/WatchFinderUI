package com.example.watchfinder.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val username: String? = null,
    val email: String? = null,
    val profileImageUrl: String? = null

)