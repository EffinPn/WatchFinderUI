package com.example.watchfinder.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchfinder.data.UiState.DetailsUiState
import com.example.watchfinder.data.dto.MovieCard
import com.example.watchfinder.data.dto.SeriesCard
import com.example.watchfinder.repository.MovieRepository
import com.example.watchfinder.repository.SeriesRepository
import com.example.watchfinder.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsVM @Inject constructor(
    private val movieRepository: MovieRepository,
    private val seriesRepository: SeriesRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun loadDetails(itemType: String, itemId: String) {
        if (_uiState.value.isLoading && (_uiState.value.movieDetail?._id == itemId || _uiState.value.seriesDetail?._id == itemId)) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null, movieDetail = null, seriesDetail = null, isFavorite = false, isSeen = false)
            }

            try {
                var loadedMovie: MovieCard? = null
                var loadedSeries: SeriesCard? = null
                val isMovie = itemType.equals("movie", ignoreCase = true)

                if (isMovie) {
                    loadedMovie = movieRepository.searchById(itemId)
                } else {
                    loadedSeries = seriesRepository.searchById(itemId)
                }

                if (loadedMovie == null && loadedSeries == null) {
                    _uiState.update { it.copy(isLoading = false, error = "Item no encontrado (ID: $itemId)") }
                    return@launch
                }

                val favMoviesDeferred = async { userRepository.getFavMovies() }
                val seenMoviesDeferred = async { userRepository.getSeenMovies() }
                val favSeriesDeferred = async { userRepository.getFavSeries() }
                val seenSeriesDeferred = async { userRepository.getSeenSeries() }
                val favMovieIds = favMoviesDeferred.await().mapNotNull { it._id }.toSet()
                val seenMovieIds = seenMoviesDeferred.await().mapNotNull { it._id }.toSet()
                val favSeriesIds = favSeriesDeferred.await().mapNotNull { it._id }.toSet()
                val seenSeriesIds = seenSeriesDeferred.await().mapNotNull { it._id }.toSet()

                val initialIsFavorite = if (isMovie) {
                    itemId in favMovieIds
                } else {
                    itemId in favSeriesIds
                }
                val initialIsSeen = if (isMovie) {
                    itemId in seenMovieIds
                } else {
                    itemId in seenSeriesIds
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        movieDetail = loadedMovie,
                        seriesDetail = loadedSeries,
                        isFavorite = initialIsFavorite,
                        isSeen = initialIsSeen
                    )
                }

            } catch (e: Exception) {
                Log.e("DetailsVM", "Error loading details or user lists: ${e.message}", e)
                _uiState.update {
                    it.copy(isLoading = false, error = "Error al cargar: ${e.message}")
                }
            }
        }
    }

    fun toggleFavorite() {
        val currentState = _uiState.value
        val itemId: String
        val itemType: String

        if (currentState.movieDetail != null) { itemId = currentState.movieDetail._id; itemType = "movie" }
        else if (currentState.seriesDetail != null) { itemId = currentState.seriesDetail._id; itemType = "series" }
        else { Log.w("DetailsVM", "toggleFavorite called but no item detail is loaded."); return }

        val currentlyFavorite = currentState.isFavorite
        _uiState.update { it.copy(isFavorite = !currentlyFavorite, error = null) }

        viewModelScope.launch {
            try {
                val success: Boolean
                if (!currentlyFavorite) {
                    Log.d("DetailsVM", "Attempting to fav item: $itemId ($itemType)")
                    success = userRepository.addToList(itemId, "fav", itemType)
                    if (!success) {
                        Log.w("DetailsVM", "Backend call FAILED for state: fav (add)")
                        _uiState.update { it.copy(isFavorite = currentlyFavorite, error = "No se pudo añadir a favoritos") } // Revertir
                    } else {
                        Log.d("DetailsVM", "Backend call successful for state: fav (add)")
                        _uiState.update { it.copy(error = null) }
                    }
                } else {
                    Log.d("DetailsVM", "Attempting to remove_fav item: $itemId ($itemType)")
                    success = userRepository.removeFromList(itemId, "fav", itemType)
                    if (!success) {
                        Log.w("DetailsVM", "Backend call FAILED for state: fav (remove)")
                        _uiState.update { it.copy(isFavorite = currentlyFavorite, error = "Error al quitar de favoritos") }
                    } else {
                        Log.d("DetailsVM", "Backend call successful for state: fav (remove)")
                        _uiState.update { it.copy(error = null) }
                    }
                }
            } catch (e: Exception) {
                Log.e("DetailsVM", "Error toggling favorite state: ${e.message}", e)
                _uiState.update { it.copy(isFavorite = currentlyFavorite, error = "Error al cambiar favorito") }
            }
        }
    }

    fun toggleSeen() {
        val currentState = _uiState.value
        val itemId: String
        val itemType: String

        if (currentState.movieDetail != null) { itemId = currentState.movieDetail._id; itemType = "movie" }
        else if (currentState.seriesDetail != null) { itemId = currentState.seriesDetail._id; itemType = "series" }
        else { return }

        val currentlySeen = currentState.isSeen
        _uiState.update { it.copy(isSeen = !currentlySeen, error = null) }

        viewModelScope.launch {
            try {
                val success: Boolean
                if (!currentlySeen) {

                    success = userRepository.addToList(itemId, "seen", itemType)
                    if (!success) {
                        _uiState.update { it.copy(isSeen = currentlySeen, error = "No se pudo marcar como visto") }
                    } else {
                        _uiState.update { it.copy(error = null) }
                    }
                } else {
                    success = userRepository.removeFromList(itemId, "seen", itemType)
                    if (!success) {
                        _uiState.update { it.copy(isSeen = currentlySeen, error = "Error al desmarcar como visto") }
                    } else {
                        _uiState.update { it.copy(error = null) }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSeen = currentlySeen, error = "Error al cambiar visto") }
            }
        }
    }
}