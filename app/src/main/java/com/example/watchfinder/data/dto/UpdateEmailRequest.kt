package com.example.watchfinder.data.dto

import com.google.gson.annotations.SerializedName

data class UpdateEmailRequest(
    @SerializedName("email")
    val email: String
)
