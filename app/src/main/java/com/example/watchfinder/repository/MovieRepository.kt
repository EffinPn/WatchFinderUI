package com.example.watchfinder.repository

import com.example.watchfinder.api.ApiService
import com.example.watchfinder.data.Utils
import com.example.watchfinder.data.dto.MovieCard
import com.example.watchfinder.data.model.Movie
import com.example.watchfinder.data.prefs.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    private val utils: Utils
) {

    suspend fun getAllMovies(): List<Movie> {
        return apiService.getMovies()
    }

    suspend fun getAllMovieCards(): List<MovieCard>{
        val apiCards = apiService.getMovies()
        return apiCards.map {
            movie -> utils.movieToCard(movie)
        }
    }

    suspend fun searchMoviesByGenre(selectedGenres: List<String>): List<MovieCard> {
        val apiCards = apiService.getMoviesByGenre(selectedGenres)
        return apiCards.map {
                movie -> utils.movieToCard(movie)
        }
    }

    suspend fun searchMoviesByTitle(userInput: String): List<MovieCard> {
        val apiCards = apiService.getMoviesByTitle(userInput)
        return apiCards.map {
                movie -> utils.movieToCard(movie)
        }
    }

    suspend fun searchById(id: String): MovieCard {
        return utils.movieToCard(apiService.getMoviesById(id))
    }

    suspend fun getMovieRecommendations(): List<MovieCard> {
        val recommendedMovies = apiService.getMovieRecommendations()
        return recommendedMovies.map { movie -> utils.movieToCard(movie) }
    }
}
