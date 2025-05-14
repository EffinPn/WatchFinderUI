package com.example.watchfinder.data.UiState

import com.example.watchfinder.data.dto.MovieCard
import com.example.watchfinder.data.dto.SeriesCard

data class SearchUiState(

    val userInput: String = "",
    val selectedSearchType: String = "Both",
    val availableGenres: List<String> = listOf("Todos"),
    val selectedGenre: Set<String> = setOf("Todos"),

    val isLoading: Boolean = false,
    val triggerNavigationToResults: Boolean = false,
    val navigationResultsMovies: List<MovieCard> = emptyList(),
    val navigationResultsSeries: List<SeriesCard> = emptyList(),
    val searchError: String? = null,
    val noResultsFound: Boolean = false
)
