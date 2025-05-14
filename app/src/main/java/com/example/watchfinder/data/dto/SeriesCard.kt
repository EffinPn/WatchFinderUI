package com.example.watchfinder.data.dto

data class SeriesCard(
    val _id: String,
    val Title: String,
    val Year: String? = null,
    val ReleaseDate: String?,
    val EndDate: String?,
    val Status: String?,
    val Seasons: String?,
    val Director: String? = null,
    val Country: String? = null,
    val Plot: String,
    val Runtime: String? = null,
    val Ratings: List<String>? = null,
    val Genres: List<String>? = null,
    val Languages: List<String>? = null,
    val Cast: List<String>? = null,
    val Rated: String?,
    val Awards: String? = null,
    val Url: String? = null,
    val Poster: String?,
    val Providers: List<String>?,
)