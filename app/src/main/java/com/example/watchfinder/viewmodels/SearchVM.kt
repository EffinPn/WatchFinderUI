
package com.example.watchfinder.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchfinder.data.UiState.SearchUiState 
import com.example.watchfinder.data.dto.MovieCard 
import com.example.watchfinder.data.dto.SeriesCard 
import com.example.watchfinder.repository.GenreRepository 
import com.example.watchfinder.repository.MovieRepository 
import com.example.watchfinder.repository.SeriesRepository 
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(
    private val genreRepository: GenreRepository,
    private val movieRepository: MovieRepository,
    private val seriesRepository: SeriesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadAvailableGenres()
    }

    private fun loadAvailableGenres() {
        viewModelScope.launch {
            try {
                val genres: List<String> = genreRepository.getAllGenres()
                _uiState.update {
                    val allGenres = listOf("Todos") + genres
                    it.copy(
                        availableGenres = allGenres,
                        selectedGenre = setOf(allGenres.firstOrNull() ?: "Todos")
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(searchError = "Error al cargar géneros: ${e.message}")
                }
            }
        }
    }

    
    fun onUserInputChange(query: String) {
        _uiState.update {
            it.copy(
                userInput = query,
                searchError = null,
                noResultsFound = false,
                selectedGenre = setOf("Todos"),
                triggerNavigationToResults = false 
            )
        }
    }

    fun onSearchTypeChange(type: String) {
        _uiState.update {
            it.copy(
                selectedSearchType = type,
                searchError = null,
                noResultsFound = false,
                triggerNavigationToResults = false 
            )
        }
    }

    fun onGenreChipClicked(genre: String) {
        _uiState.update { currentState ->
            val currentSelection = currentState.selectedGenre.toMutableSet()
            val isTodosSelected = currentSelection.contains("Todos")

            if (genre == "Todos") {
                currentSelection.clear()
                currentSelection.add("Todos")
            } else {
                currentSelection.remove("Todos")
                if (currentSelection.contains(genre)) {
                    currentSelection.remove(genre)
                } else {
                    currentSelection.add(genre)
                }
                if (currentSelection.isEmpty()) {
                    currentSelection.add("Todos")
                }
            }

            currentState.copy(
                selectedGenre = currentSelection,
                userInput = "",
                searchError = null,
                noResultsFound = false,
                triggerNavigationToResults = false 
            )
        }
    }

    
    fun performSearch() {
        searchJob?.cancel() 
        _uiState.update {
            it.copy(
                isLoading = true,
                searchError = null,
                noResultsFound = false,
                triggerNavigationToResults = false, 
                navigationResultsMovies = emptyList(), 
                navigationResultsSeries = emptyList()
            )
        }
        val currentState = _uiState.value 

        searchJob = viewModelScope.launch {
            try {
                
                val moviesResult = mutableListOf<MovieCard>()
                val seriesResult = mutableListOf<SeriesCard>()

                val genresToSearch = currentState.selectedGenre.filter { it != "Todos" }.toList()

                if (genresToSearch.isNotEmpty()) {
                    
                    println("Searching by Genres: ${genresToSearch}, Type: ${currentState.selectedSearchType}")
                    when (currentState.selectedSearchType) {
                        "Movies" -> moviesResult.addAll(movieRepository.searchMoviesByGenre(genresToSearch))
                        "Series" -> seriesResult.addAll(seriesRepository.searchSeriesByGenre(genresToSearch).filterIsInstance<SeriesCard>()) 
                        "Both" -> {
                            moviesResult.addAll(movieRepository.searchMoviesByGenre(genresToSearch))
                            seriesResult.addAll(seriesRepository.searchSeriesByGenre(genresToSearch).filterIsInstance<SeriesCard>()) 
                        }
                    }
                } else if (currentState.userInput.isNotBlank()) {
                    
                    println("Searching by Title: ${currentState.userInput}, Type: ${currentState.selectedSearchType}")
                    when (currentState.selectedSearchType) {
                        "Movies" -> moviesResult.addAll(movieRepository.searchMoviesByTitle(currentState.userInput))
                        "Series" -> seriesResult.addAll(seriesRepository.searchSeriesByTitle(currentState.userInput).filterIsInstance<SeriesCard>()) 
                        "Both" -> {
                            moviesResult.addAll(movieRepository.searchMoviesByTitle(currentState.userInput))
                            seriesResult.addAll(seriesRepository.searchSeriesByTitle(currentState.userInput).filterIsInstance<SeriesCard>()) 
                        }
                    }
                } else {
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            searchError = "Introduce un título o selecciona uno o más géneros"
                        )
                    }
                    return@launch 
                }

                
                val noResults = moviesResult.isEmpty() && seriesResult.isEmpty()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        noResultsFound = noResults, 
                        
                        navigationResultsMovies = moviesResult,
                        navigationResultsSeries = seriesResult,
                        
                        triggerNavigationToResults = !noResults
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, searchError = "Error en la búsqueda: ${e.message}")
                }
                e.printStackTrace() 
            }
        }
    }

    
    fun onResultsNavigated() {
        _uiState.update { it.copy(triggerNavigationToResults = false) }
    }


    
    fun resetSearch() {
        searchJob?.cancel() 
        _uiState.update {
            
            SearchUiState(
                availableGenres = it.availableGenres,
                selectedGenre = setOf(it.availableGenres.firstOrNull() ?: "Todos")
                
            )
        }
    }
}
