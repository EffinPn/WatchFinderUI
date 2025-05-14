package com.example.watchfinder.screens

import androidx.compose.runtime.Composable
import com.example.watchfinder.R


@Composable
fun providerToLogo(providerName: String): Int? {

    return when (providerName) {
        "Netflix", "Netflix Standard with Ads" -> R.drawable.netflix
        "Max" -> R.drawable.max
        "Disney Plus" -> R.drawable.disney
        "Amazon Prime Video", -> R.drawable.prime
        "Movistar Plus+" -> R.drawable.mplus
        "AMC Plus Apple TV Channel" -> R.drawable.amcapple
        "Acontra Plus", "Acontra Plus Amazon Channel" -> R.drawable.acontra
        "FilmBox+" -> R.drawable.filmbox
        "Filmin" -> R.drawable.filmin
        "FlixOlé", "FlixOlé Amazon Channel"  -> R.drawable.flixole
        "Lionsgate+ Amazon Channels" -> R.drawable.lionsgate
        "MGM Plus Amazon Channel"   -> R.drawable.mgm
        "SkyShowtime" -> R.drawable.skyshow
        "Tivify" -> R.drawable.tivify
        "fuboTV" -> R.drawable.fubotv
        else -> null
    }
}