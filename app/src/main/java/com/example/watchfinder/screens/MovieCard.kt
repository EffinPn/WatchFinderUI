package com.example.watchfinder.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.watchfinder.R
import com.example.watchfinder.data.dto.MovieCard

@Composable
fun MovieCard(
    movie: MovieCard,
    showActions: Boolean = true,
    onFavoriteClick: () -> Unit = {},
    isFavorite: Boolean,
    onSeenClick: () -> Unit = {},
    isSeen: Boolean,
    playWhenReady: Boolean
) {

    Card(
        modifier = Modifier
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .clip(CardDefaults.shape)
            ) {
                val videoUrl = movie.Url
                if (!videoUrl.isNullOrBlank()) {
                    VideoPlayer(
                        videoUrl = videoUrl,
                        playWhenReady = playWhenReady
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Trailer no disponible")
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(

                        Brush.verticalGradient(
                            0.0f to Color.Black.copy(alpha = 0.0f),
                            0.3f to Color.Black.copy(alpha = 0.75f),
                            0.6f to Color.Black.copy(alpha = 0.99f),
                            0.95f to Color.Black
                        )
                    )
                    .padding(10.dp)
            ) {

                movie.Country?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
                Text(
                    movie.Title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.Runtime ?: "Duración no disponible",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    val languages = movie.Languages
                    val languagesToShow = languages?.takeIf { it.isNotEmpty() }?.let { langs ->
                        val maxToShow = 2
                        langs.take(maxToShow).joinToString(", ") +
                                if (langs.size > maxToShow) ", ..." else ""
                    } ?: "Idiomas no disp."
                    Text(

                        languagesToShow,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray

                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    movie.Plot ?: "No disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    movie.Director ?: "No disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(3.dp))

                val ratings = movie.Ratings
                val ratingsToShow = ratings?.takeIf { it.isNotEmpty() }?.let { rtngs ->
                    val maxToShow = 3
                    rtngs.take(maxToShow).joinToString(", ") +
                            if (rtngs.size > maxToShow) ", ..." else ""
                } ?: "Ratings no disp."
                Text(
                    ratingsToShow,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(3.dp))

                val cast = movie.Cast
                val castToShow = cast?.takeIf { it.isNotEmpty() }?.let { cst ->
                    val maxToShow = 3
                    cst.take(maxToShow).joinToString(", ") +
                            if (cast.size > maxToShow) ", ..." else ""
                } ?: "Reparto no disp."
                Text(
                    castToShow,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )


                Spacer(modifier = Modifier.height(3.dp))

                val genres = movie.Genres
                Row(modifier = Modifier.fillMaxWidth().height(35.dp)){
                    if (genres != null) {
                        genres.forEach { genre ->
                            val genreResId = GenretoIcon(genre)
                            // -------------------------
                            if (genreResId != null) {
                                Image(
                                    painter = painterResource(id = genreResId),
                                    contentDescription = genre,
                                )
                            }
                        }
                    }
                }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        movie.Awards ?: "No disponible",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
            }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                if (showActions) {
                    IconButton(
                        onClick = onFavoriteClick,
                    ) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            painter = if (isFavorite) painterResource(id = R.drawable.heartfill) else painterResource(
                                id = R.drawable.heart
                            ),
                            contentDescription = "Añadir a Favoritos",
                            tint = if (isFavorite) {
                                Color.Red
                            } else {
                                Color.White
                            }
                        )
                    }

                    IconButton(
                        onClick = onSeenClick,
                        modifier = Modifier
                            .padding(1.dp)
                    ) {
                        Icon(
                            modifier = Modifier.padding(4.dp),
                            painter = if (isSeen) painterResource(id = R.drawable.eyeno) else painterResource(
                                id = R.drawable.eye
                            ),
                            contentDescription = "Marcar como Visto",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                val plat = movie.Providers
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    if (plat != null) {
                        plat.forEach { providerName ->
                            val logoResId = providerToLogo(providerName)
                            if (logoResId != null) {
                                Image(
                                    painter = painterResource(id = logoResId),
                                    contentDescription = providerName,
                                    modifier = Modifier.height(25.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
}

