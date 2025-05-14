package com.example.watchfinder.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MovieOrSeries(    onMoviesClicked: () -> Unit,
                      onSeriesClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            modifier = Modifier.fillMaxWidth().padding(10.dp).height(50.dp),
            onClick = onMoviesClicked) { Text("Quiero ver Pelis") }

        Button(
            modifier = Modifier.fillMaxWidth().padding(10.dp).height(50.dp),
            onClick = onSeriesClicked ) { Text("Quiero ver Series") }
    }
}