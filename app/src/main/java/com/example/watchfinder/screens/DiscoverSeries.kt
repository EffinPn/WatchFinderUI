package com.example.watchfinder.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.watchfinder.R
import com.example.watchfinder.viewmodels.DiscoverSeriesVM
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun DiscoverSeries(discoverViewModel: DiscoverSeriesVM = hiltViewModel()) {

    val uiState by discoverViewModel.uiState.collectAsState()
    val currentCards = uiState.cards
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val limit = screenWidth
    val exit = (screenWidth.value * 6f)
    val offSetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val currentSeries = currentCards.getOrNull(0)
    val nextSeries = currentCards.getOrNull(1)
    val favoriteIds = uiState.favoriteSeriesIds
    val seenIds = uiState.seenSeriesIds
    var showAnimationIcon by remember { mutableStateOf(false) }
    var animationIconType by remember { mutableStateOf<Int?>(null) }
    val iconOffsetY = remember { Animatable(0f) }
    val iconAlpha = remember { Animatable(0f) }


    fun triggerIconAnimation(isLike: Boolean) {
        scope.launch {
            showAnimationIcon = true
            animationIconType = if (isLike) {
                R.drawable.heartfilllike
            } else {
                R.drawable.cross
            }
            iconOffsetY.snapTo(0f)
            iconAlpha.snapTo(1f)

            launch {
                iconOffsetY.animateTo(
                    targetValue = -150f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = LinearOutSlowInEasing
                    )
                )
            }
            launch {
                iconAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = EaseIn
                    )
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        when {
            uiState.isLoading && currentCards.isEmpty() -> {
                Progress()
            }

            uiState.error != null -> {
                Text("Error ${uiState.error}")
            }

            currentCards.isEmpty() && !uiState.isLoading -> {
                Text("¡Eso es todo por ahora!")
            }

            currentSeries != null -> {

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (nextSeries != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            val isNextFavorite = nextSeries._id in favoriteIds
                            val isNextSeen = nextSeries._id in seenIds

                            SeriesCard(
                                series = nextSeries,
                                isFavorite = isNextFavorite,
                                isSeen = isNextSeen,
                                onFavoriteClick = {},
                                onSeenClick = {},
                                playWhenReady = false
                            )
                        }
                    }

                    if (showAnimationIcon && animationIconType != null) {
                        Icon(
                            painter = painterResource(id = animationIconType!!),
                            contentDescription = if (animationIconType == R.drawable.heartfilllike) "Like Animation" else "Dislike Animation",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = iconOffsetY.value.dp)
                                .alpha(iconAlpha.value),
                            tint = Color.Red
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .offset { IntOffset(offSetX.value.roundToInt(), 0) }
                            .graphicsLayer {
                                rotationZ = offSetX.value / 30f
                                transformOrigin = TransformOrigin(0.5f, 0.5f)
                                scaleX = 1f - (abs(offSetX.value) / (screenWidth.value * 6))
                                scaleY = 1f - (abs(offSetX.value) / (screenWidth.value * 6))
                            }
                            .pointerInput(currentSeries._id) {
                                detectHorizontalDragGestures(
                                    onDragStart = {},
                                    onDragEnd = {
                                        val final = offSetX.value
                                        if (abs(final) > limit.value) {
                                            val target = if (final > 0) exit else -exit
                                            val isLike =
                                                final > 0
                                            triggerIconAnimation(isLike = isLike)

                                            scope.launch {
                                                offSetX.animateTo(
                                                    targetValue = target,
                                                    animationSpec = tween(durationMillis = 400)
                                                )
                                                if (final > 0) {
                                                    discoverViewModel.cardLiked(currentSeries)
                                                } else {
                                                    discoverViewModel.cardDisliked(currentSeries)
                                                }
                                                offSetX.snapTo(0f)
                                            }
                                        } else {
                                            scope.launch {
                                                offSetX.animateTo(
                                                    0f
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
                            .fillMaxHeight(0.85f)
                            .fillMaxWidth()
                    ) {
                        val isCurrentFavorite = currentSeries._id in favoriteIds
                        val isCurrentSeen = currentSeries._id in seenIds

                        SeriesCard(
                            series = currentSeries,
                            isFavorite = isCurrentFavorite,
                            isSeen = isCurrentSeen,

                            onFavoriteClick = { discoverViewModel.cardFav(currentSeries) },
                            onSeenClick = { discoverViewModel.cardSeen(currentSeries) },
                            playWhenReady = true
                        )
                    }
                }
            }
        }
    }
}