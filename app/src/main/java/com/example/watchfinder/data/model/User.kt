package com.example.watchfinder.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String,
    var likedMovies: MutableList<@Serializable Movie>? = null,
    var dislikedMovies: MutableList<@Serializable Movie>? = null,
    var likedSeries: MutableList<@Serializable Series>? = null,
    var dislikedSeries: MutableList<@Serializable Series>? = null,
    var seenMovies: MutableList<@Serializable Movie>? = null,
    var seenSeries: MutableList<@Serializable Series>? = null,
    var favMovies: MutableList<@Serializable Movie>? = null,
    var favSeries: MutableList<@Serializable Series>? = null,
    val profileImageUrl: String? = null
){
    fun addLikedMovie(movie:Movie){
        this.likedMovies?.add(movie)
    }
    fun addDislikedMovie(movie:Movie){
        this.dislikedMovies?.add(movie)
    }
    fun addSeenMovie(movie:Movie){
        this.seenMovies?.add(movie)
    }
    fun addFavMovie(movie:Movie){
        this.favMovies?.add(movie)
    }

    fun addLikedSeries(series:Series){
        this.likedSeries?.add(series)
    }
    fun addDislikedSeries(series:Series){
        this.dislikedSeries?.add(series)
    }
    fun addSeenSeries(series:Series){
        this.seenSeries?.add(series)
    }
    fun addFavSeries(series:Series){
        this.favSeries?.add(series)
    }

}
