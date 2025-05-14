package com.example.watchfinder.data.UiState

import com.example.watchfinder.data.dto.SeriesCard

data class DiscoverSeriesUiState(
    val cards: List<SeriesCard> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val finished: Boolean = false,
    val favoriteSeriesIds: Set<String> = emptySet(),
    val seenSeriesIds: Set<String> = emptySet()
)