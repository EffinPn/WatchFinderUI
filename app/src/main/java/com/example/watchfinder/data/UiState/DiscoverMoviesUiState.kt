package com.example.watchfinder.data.UiState

import com.example.watchfinder.data.dto.MovieCard

data class DiscoverMoviesUiState(
    val cards: List<MovieCard> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val finished: Boolean = false,
    val favoriteMovieIds: Set<String> = emptySet(),
    val seenMovieIds: Set<String> = emptySet()
)