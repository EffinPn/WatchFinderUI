package com.example.watchfinder.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchfinder.data.UiState.DiscoverMoviesUiState
import com.example.watchfinder.data.dto.MovieCard
import com.example.watchfinder.repository.MovieRepository
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
class DiscoverMoviesVM @Inject constructor(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverMoviesUiState())
    val uiState: StateFlow<DiscoverMoviesUiState> = _uiState.asStateFlow()

    private var fetchJob: Job? = null

    
    private val RELOAD_THRESHOLD = 3 
    

    init {
        Log.e("DiscoverMoviesVM", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        loadInitialData() 
    }

    fun loadInitialData() {
        
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            Log.e("DiscoverMoviesVM", "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
            _uiState.update { it.copy(isLoading = true, error = null, finished = false) } 
            try {
                
                val favMoviesDeferred = async { userRepository.getFavMovies() }
                val seenMoviesDeferred = async { userRepository.getSeenMovies() }
                val recommendationsDeferred = async { movieRepository.getMovieRecommendations() }

                val favMoviesResult = favMoviesDeferred.await()
                val seenMoviesResult = seenMoviesDeferred.await()
                val newRecommendations = recommendationsDeferred.await()

                val favIds = favMoviesResult.mapNotNull { it._id }.toSet()
                val seenIds = seenMoviesResult.mapNotNull { it._id }.toSet()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        cards = newRecommendations.distinctBy { card -> card._id }, 
                        favoriteMovieIds = favIds,
                        seenMovieIds = seenIds,
                        finished = newRecommendations.isEmpty() 
                    )
                }
            } catch (e: Exception) {
                Log.e("DiscoverMoviesVM", "Error loading initial data: ${e.message}", e)
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
                
                
                val newRecommendations = movieRepository.getMovieRecommendations()

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
                Log.e("DiscoverMoviesVM", "Error fetching more recommendations: ${e.message}", e)
                
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

    fun cardLiked(likedCard: MovieCard) {
        
        viewModelScope.launch {
            try {
                userRepository.addToList(likedCard._id, "liked", "movie")
                
                
                
                
            } catch (e: Exception) {
                Log.e("DiscoverMoviesVM", "Error registering Like: ${e.message}")
            }
        }
        
        cardSwiped(likedCard._id)
    }

    fun cardDisliked(dislikedCard: MovieCard) {
        viewModelScope.launch {
            try {
                userRepository.addToList(dislikedCard._id, "disliked", "movie")
            } catch (e: Exception) {
                Log.e("DiscoverMoviesVM", "Error registering Dislike: ${e.message}")
            }
        }
        cardSwiped(dislikedCard._id)
    }

    fun cardFav(favCard: MovieCard) {
        val currentFavIds = _uiState.value.favoriteMovieIds
        val isCurrentlyFavorite = favCard._id in currentFavIds
        viewModelScope.launch {
            try {
                val success = if (isCurrentlyFavorite) {
                    userRepository.removeFromList(favCard._id, "fav", "movie")
                } else {
                    userRepository.addToList(favCard._id, "fav", "movie")
                }
                if (success) {
                    _uiState.update {
                        it.copy(favoriteMovieIds = if (isCurrentlyFavorite) currentFavIds - favCard._id else currentFavIds + favCard._id)
                    }
                    
                    
                } else {
                    Log.w("DiscoverMoviesVM", "Failed to toggle favorite state for ${favCard._id}")
                }
            } catch (e: Exception) {
                Log.e("DiscoverMoviesVM", "Error toggling Fav: ${e.message}")
            }
        }
    }

    fun cardSeen(seenCard: MovieCard) {
        val currentSeenIds = _uiState.value.seenMovieIds
        val isCurrentlySeen = seenCard._id in currentSeenIds
        viewModelScope.launch {
            try {
                val success = if (isCurrentlySeen) {
                    userRepository.removeFromList(seenCard._id, "seen", "movie")
                } else {
                    userRepository.addToList(seenCard._id, "seen", "movie")
                }
                if (success) {
                    _uiState.update {
                        it.copy(seenMovieIds = if (isCurrentlySeen) currentSeenIds - seenCard._id else currentSeenIds + seenCard._id)
                    }
                } else {
                    Log.w("DiscoverMoviesVM", "Failed to toggle seen state for ${seenCard._id}")
                }
            } catch (e: Exception) {
                Log.e("DiscoverMoviesVM", "Error toggling Seen: ${e.message}")
            }
        }
    }
}