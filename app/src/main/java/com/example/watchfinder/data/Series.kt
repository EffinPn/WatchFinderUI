package com.example.watchfinder.data

data class Series(
    val _id: String,
    val title: String,
    val year: Int,
    val releaseDate: String,
    val endDate: String,
    val status: String,
    val seasons: String,
    val director: String,
    val country: String,
    val plot: String,
    val runtime: String,
    val ratings: List<String>,
    val genres: List<String>,
    val languages: List<String>,
    val cast: List<String>,
    val rated: String,
    val awards: String
)
