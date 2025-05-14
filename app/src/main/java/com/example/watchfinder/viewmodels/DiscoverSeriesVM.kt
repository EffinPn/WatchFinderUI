package com.example.watchfinder.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchfinder.data.UiState.DiscoverSeriesUiState
import com.example.watchfinder.data.dto.SeriesCard
import com.example.watchfinder.repository.SeriesRepository
import com.example.watchfinder.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverSeriesVM @Inject constructor(
    private val userRepository: UserRepository,
    private val seriesRepository: SeriesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverSeriesUiState())
    val uiState: StateFlow<DiscoverSeriesUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null
    private val RELOAD_THRESHOLD = 3 

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, finished = false) }
            try {
                val favSeriesDeferred = async { userRepository.getFavSeries() }
                val seenSeriesDeferred = async { userRepository.getSeenSeries() }
                val recommendationsDeferred = async { seriesRepository.getSeriesRecommendations() }

                val favSeriesResult = favSeriesDeferred.await()
                val seenSeriesResult = seenSeriesDeferred.await()
                val newRecommendations = recommendationsDeferred.await()

                val favIds = favSeriesResult.mapNotNull { it._id }.toSet()
                val seenIds = seenSeriesResult.mapNotNull { it._id }.toSet()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        cards = newRecommendations.distinctBy { card -> card._id },
                        favoriteSeriesIds = favIds,
                        seenSeriesIds = seenIds,
                        finished = newRecommendations.isEmpty()
                    )
                }
            } catch (e: Exception) {
                Log.e("DiscoverSeriesVM", "Error loading initial data: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false, error = "Error al cargar datos: ${e.message ?: "Desconocido"}") }
            }
        }
    }

    private fun fetchMoreRecommendations() {
        if (_uiState.value.isLoading || _uiState.value.finished) return

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val newRecommendations = seriesRepository.getSeriesRecommendations()

                _uiState.update { currentState ->
                    if (newRecommendations.isEmpty()) {
                        currentState.copy(isLoading = false, finished = true)
                    } else {
                        val existingCardIds = currentState.cards.map { it._id }.toSet()
                        val trulyNewCards = newRecommendations.filterNot { it._id in existingCardIds }

                        currentState.copy(
                            isLoading = false,
                            cards = (currentState.cards + trulyNewCards).distinctBy { it._id },
                            finished = trulyNewCards.isEmpty()
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("DiscoverSeriesVM", "Error fetching more recommendations: ${e.message}", e)
                _uiState.update { it.copy(isLoading = false, error = "Error al cargar más: ${e.message ?: "Desconocido"}") }
            }
        }
    }

    private fun cardSwiped(swipedCardId: String) {
        _uiState.update { currentState ->
            val remainingCards = currentState.cards.filterNot { it._id == swipedCardId }
            if (remainingCards.size < RELOAD_THRESHOLD && !currentState.isLoading && !currentState.finished) {
                fetchMoreRecommendations()
            }
            currentState.copy(cards = remainingCards)
        }
    }

    fun cardLiked(likedCard: SeriesCard) {
        viewModelScope.launch {
            try {
                userRepository.addToList(likedCard._id, "liked", "series")
            } catch (e: Exception) {
                Log.e("DiscoverSeriesVM", "Error registering Like: ${e.message}")
            }
        }
        cardSwiped(likedCard._id)
    }

    fun cardDisliked(dislikedCard: SeriesCard) {
        viewModelScope.launch {
            try {
                userRepository.addToList(dislikedCard._id, "disliked", "series")
            } catch (e: Exception) {
                Log.e("DiscoverSeriesVM", "Error registering Dislike: ${e.message}")
            }
        }
        cardSwiped(dislikedCard._id)
    }

    fun cardFav(favCard: SeriesCard) {
        val currentFavIds = _uiState.value.favoriteSeriesIds
        val isCurrentlyFavorite = favCard._id in currentFavIds
        viewModelScope.launch {
            try {
                val success = if (isCurrentlyFavorite) {
                    userRepository.removeFromList(favCard._id, "fav", "series")
                } else {
                    userRepository.addToList(favCard._id, "fav", "series")
                }
                if (success) {
                    _uiState.update {
                        it.copy(favoriteSeriesIds = if (isCurrentlyFavorite) currentFavIds - favCard._id else currentFavIds + favCard._id)
                    }
                } else {
                    Log.w("DiscoverSeriesVM", "Failed to toggle favorite state for ${favCard._id}")
                }
            } catch (e: Exception) {
                Log.e("DiscoverSeriesVM", "Error toggling Fav: ${e.message}")
            }
        }
    }

    fun cardSeen(seenCard: SeriesCard) {
        val currentSeenIds = _uiState.value.seenSeriesIds
        val isCurrentlySeen = seenCard._id in currentSeenIds
        viewModelScope.launch {
            try {
                val success = if (isCurrentlySeen) {
                    userRepository.removeFromList(seenCard._id, "seen", "series")
                } else {
                    userRepository.addToList(seenCard._id, "seen", "series")
                }
                if (success) {
                    _uiState.update {
                        it.copy(seenSeriesIds = if (isCurrentlySeen) currentSeenIds - seenCard._id else currentSeenIds + seenCard._id)
                    }
                } else {
                    Log.w("DiscoverSeriesVM", "Failed to toggle seen state for ${seenCard._id}")
                }
            } catch (e: Exception) {
                Log.e("DiscoverSeriesVM", "Error toggling Seen: ${e.message}")
            }
        }
    }
}