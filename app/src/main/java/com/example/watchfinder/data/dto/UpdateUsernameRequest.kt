package com.example.watchfinder.data.dto

import com.google.gson.annotations.SerializedName

class UpdateUsernameRequest(
    @SerializedName("userName")
    val userName: String
)
