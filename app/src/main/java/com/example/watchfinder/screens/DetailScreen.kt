package com.example.watchfinder.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watchfinder.viewmodels.DetailsVM
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun DetailScreen(
    itemType: String,
    itemId: String,
    onNavigateBack: () -> Unit,
    viewModel: DetailsVM = hiltViewModel()
) {
    val offSetX = remember { Animatable(0f) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val limit = screenWidth
    val exit = (screenWidth.value * 6f)
    val scope = rememberCoroutineScope()

    LaunchedEffect(itemType, itemId) {
        viewModel.loadDetails(itemType, itemId)
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            uiState.isLoading -> {
                Progress()
            }

            uiState.error != null -> {
                Text("Error: ${uiState.error}")
            }

            uiState.movieDetail != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .fillMaxSize()
                        .offset { IntOffset(offSetX.value.roundToInt(), 0) }
                        .graphicsLayer {
                            rotationZ = offSetX.value / 30f

                            transformOrigin = TransformOrigin(0.5f, 0.5f)

                            scaleX = 1f - (abs(offSetX.value) / (screenWidth.value * 6))
                            scaleY = 1f - (abs(offSetX.value) / (screenWidth.value * 6))
                        }
                        .pointerInput(itemId) {

                            detectHorizontalDragGestures(
                                onDragStart = {},
                                onDragEnd = {
                                    val final = offSetX.value
                                    if (abs(final) > limit.value) {
                                        val target =
                                            if (final > 0) exit else -exit

                                        scope.launch {
                                            offSetX.animateTo(
                                                targetValue = target,
                                                animationSpec = tween(durationMillis = 300)
                                            ) {
                                                onNavigateBack()
                                            }
                                        }
                                    } else {
                                        scope.launch {
                                            offSetX.animateTo(
                                                targetValue = 0f,
                                                animationSpec = tween(durationMillis = 300)
                                            )
                                        }
                                    }
                                }

                            ) { change, dragAmount ->
                                println("Dragging, OffsetX: ${offSetX.value}")
                                change.consume()
                                scope.launch {
                                    offSetX.snapTo(offSetX.value + dragAmount)
                                }
                            }
                        }
                ) {

                    MovieCard(
                        movie = uiState.movieDetail!!,
                        isFavorite = uiState.isFavorite,
                        isSeen = uiState.isSeen,
                        onFavoriteClick = {
                            viewModel.toggleFavorite()
                        },
                        onSeenClick = {
                            viewModel.toggleSeen()
                        },
                        playWhenReady = true
                    )
                }
            }

            uiState.seriesDetail != null -> {
                Box(

                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .fillMaxSize()
                        .offset { IntOffset(offSetX.value.roundToInt(), 0) }
                        .graphicsLayer {
                            rotationZ = offSetX.value / 30f

                            transformOrigin = TransformOrigin(0.5f, 0.5f)

                            scaleX = 1f - (abs(offSetX.value) / (screenWidth.value * 6))
                            scaleY = 1f - (abs(offSetX.value) / (screenWidth.value * 6))

                        }

                        .pointerInput(itemId) {
                            detectHorizontalDragGestures(

                                onDragStart = {},
                                onDragEnd = {
                                    val final = offSetX.value
                                    if (abs(final) > limit.value) {
                                        val target =
                                            if (final > 0) exit else -exit

                                        scope.launch {
                                            offSetX.animateTo(
                                                targetValue = target,
                                                animationSpec = tween(durationMillis = 300)
                                            ) {
                                                onNavigateBack()
                                            }
                                        }
                                    } else {
                                        scope.launch {
                                            offSetX.animateTo(
                                                targetValue = 0f,
                                                animationSpec = tween(durationMillis = 300)
                                            )
                                        }
                                    }
                                }

                            ) { change, dragAmount ->
                                change.consume()
                                scope.launch {
                                    offSetX.snapTo(offSetX.value + dragAmount)
                                }
                            }
                        }
                ) {

                    SeriesCard(
                        series = uiState.seriesDetail!!,
                        isFavorite = uiState.isFavorite,
                        isSeen = uiState.isSeen,
                        onFavoriteClick = {
                            viewModel.toggleFavorite()
                        },
                        onSeenClick = {
                            viewModel.toggleSeen()
                        },
                        playWhenReady = true
                    )
                }
            }

            else -> {
                Text("No se encontraron detalles.")
            }
        }
    }
}