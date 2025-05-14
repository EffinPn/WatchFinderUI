package com.example.watchfinder.data.dto

import com.google.gson.annotations.SerializedName

data class ProfileImageUpdateResponse(
    @SerializedName("profileImageUrl")
    val profileImageUrl: String
)