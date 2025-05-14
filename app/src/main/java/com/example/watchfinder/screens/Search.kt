package com.example.watchfinder.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.watchfinder.data.dto.MovieCard
import com.example.watchfinder.data.dto.SeriesCard
import com.example.watchfinder.viewmodels.SearchVM

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Search(
    viewModel: SearchVM = hiltViewModel(),
    onNavigateToDetail: (itemType: String, itemId: String) -> Unit,
    onNavigateToResults: (movies: List<MovieCard>, series: List<SeriesCard>) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.triggerNavigationToResults) {
        if (uiState.triggerNavigationToResults) {

            onNavigateToResults(
                uiState.navigationResultsMovies,
                uiState.navigationResultsSeries
            )

            viewModel.onResultsNavigated()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Búsqueda", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        TextField(
            value = uiState.userInput,
            onValueChange = viewModel::onUserInputChange,
            label = { Text("Introduce título...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = (uiState.selectedGenre == setOf("Todos")),
        )
        Spacer(Modifier.height(16.dp))

        SearchTypeSelectorComponent(
            selectedOption = uiState.selectedSearchType,
            onOptionSelected = viewModel::onSearchTypeChange
        )
        Spacer(Modifier.height(16.dp))

        Text(
            "Seleccionar Género:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            uiState.availableGenres.forEach { genre ->
                val isSelected = genre in uiState.selectedGenre
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.onGenreChipClicked(genre) },
                    label = { Text(genre) },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Seleccionado",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    }
                )
            }
        }
        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = viewModel::performSearch,
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Buscar")
                }
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = viewModel::resetSearch) {
                Text("Reset")
            }
        }
        Spacer(Modifier.height(16.dp))

        if (uiState.searchError != null) {
            Text(
                text = uiState.searchError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else if (uiState.noResultsFound && !uiState.isLoading) {
            Text(
                text = "No se encontraron resultados.",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun SearchResultItem(
    title: String,
    posterPath: String?,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = posterPath,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                onError = { error ->
                    Log.e("SearchResultItemDebug", "Coil Error loading $posterPath: ${error.result.throwable}")
                }

            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SearchTypeSelectorComponent(
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    val options = listOf("Movies", "Series", "Both")

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            options.forEach { optionText ->
                Row(
                    Modifier
                        .selectable(
                            selected = (selectedOption == optionText),
                            onClick = { onOptionSelected(optionText) },
                            role = Role.RadioButton
                        )
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedOption == optionText),
                        onClick = null
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(text = optionText)
                }
            }
        }
    }
}