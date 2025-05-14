package com.example.watchfinder.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watchfinder.data.dto.MovieCard
import com.example.watchfinder.data.dto.SeriesCard
import com.example.watchfinder.viewmodels.MyContentVM

@Composable
fun MyContent(
    viewModel: MyContentVM = hiltViewModel(),
    onNavigateToDetail: (itemType: String, itemId: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState.currentView) {
        MyContentView.BUTTONS -> {
            MyContentButtonView(
                onMoviesClick = viewModel::loadFavoriteMovies,
                onSeriesClick = viewModel::loadFavoriteSeries,
                error = uiState.error
            )
        }
        MyContentView.MOVIES -> {
            MyContentListView(
                isLoading = uiState.isLoading,
                error = uiState.error,
                items = uiState.favoriteMovies,
                itemType = "movie",
                onNavigateBack = viewModel::showButtonsView,
                onNavigateToDetail = onNavigateToDetail
            )
        }
        MyContentView.SERIES -> {
            MyContentListView(
                isLoading = uiState.isLoading,
                error = uiState.error,
                items = uiState.favoriteSeries,
                itemType = "series",
                onNavigateBack = viewModel::showButtonsView,
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}

@Composable
fun MyContentButtonView(
    onMoviesClick: () -> Unit,
    onSeriesClick: () -> Unit,
    error: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(modifier = Modifier.fillMaxWidth().padding(10.dp).height(50.dp), onClick = onMoviesClick) {
            Text("Películas Favoritas")
        }
        Button(modifier = Modifier.fillMaxWidth().padding(10.dp).height(50.dp), onClick = onSeriesClick) {
            Text("Series Favoritas")
        }
    }
}

@Composable
fun <T> MyContentListView(
    isLoading: Boolean,
    error: String?,
    items: List<T>,
    itemType: String,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (itemType: String, itemId: String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }

        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Progress()
                }
            }
            error != null -> {
                Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("Error: $error", color = MaterialTheme.colorScheme.error)
                }
            }
            items.isEmpty() -> {
                Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                    Text("No tienes ${itemType}s favoritas añadidas.")
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 120.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items) { item ->
                        val title: String
                        val posterPath: String?
                        val id: String

                        when (item) {
                            is MovieCard -> {
                                title = item.Title
                                posterPath = item.Poster
                                id = item._id ?: ""
                            }
                            is SeriesCard -> {
                                title = item.Title
                                posterPath = item.Poster
                                id = item._id ?: ""
                            }
                            else -> return@items
                        }

                        if (id.isNotEmpty()) {
                            SearchResultItem(
                                title = title,
                                posterPath = posterPath,
                                onClick = { onNavigateToDetail(itemType, id) }
                            )
                        }
                    }
                }
            }
        }
    }
}