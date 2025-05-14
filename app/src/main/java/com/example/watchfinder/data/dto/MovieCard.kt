package com.example.watchfinder.data.dto

data class MovieCard(

    val _id: String,
    val Title: String,
    val Plot: String,
    val Genres: List<String>? = null,
    val Runtime: String? = null,
    val Director: String? = null,
    val Cast: List<String>? = null,
    val Ratings: List<String>? = null,
    val Languages: List<String>? = null,
    val Country: String? = null,
    val Awards: String? = null,
    val Year: Int? = null,
    val Url: String,
    val Rated: String?,
    val ReleaseDate: String?,
    val Poster: String?,
    val Providers: List<String>?,

)
