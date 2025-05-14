package com.example.watchfinder.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Series(
    val _id: String,
    val title: String,
    val year: String?,
    val releaseDate: String?,
    val endDate: String?,
    val status: String?,
    val seasons: String?,
    val director: String?,
    val country: String?,
    val plot: String?,
    val runtime: String?,
    val ratings: List<String>?,
    val genres: List<String>?,
    val languages: List<String>?,
    val cast: List<String>?,
    val rated: String?,
    val awards: String?,
    val url: String? = null,
    val poster: String?,
    val providers: List<String>?
)
