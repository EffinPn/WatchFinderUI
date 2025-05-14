
package com.example.watchfinder.viewmodels

import MyContentUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.watchfinder.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyContentVM @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyContentUiState())
    val uiState: StateFlow<MyContentUiState> = _uiState.asStateFlow()

    fun loadFavoriteMovies() {
        if (_uiState.value.isLoading) return 

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, currentView = MyContentView.MOVIES) } 
            try {
                
                
                val movies = userRepository.getFavMovies()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        favoriteMovies = userRepository.getFavMovies()
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar películas favoritas: ${e.message}",
                        currentView = MyContentView.BUTTONS 
                    )
                }
                e.printStackTrace()
            }
        }
    }

    fun loadFavoriteSeries() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, currentView = MyContentView.SERIES) }
            try {
                
                
                val series = userRepository.getFavSeries()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        favoriteSeries = userRepository.getFavSeries()
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar series favoritas: ${e.message}",
                        currentView = MyContentView.BUTTONS 
                    )
                }
                e.printStackTrace()
            }
        }
    }

    
    fun showButtonsView() {
        _uiState.update {
            it.copy(
                currentView = MyContentView.BUTTONS,
                error = null, 
                
                
                
            )
        }
    }
}