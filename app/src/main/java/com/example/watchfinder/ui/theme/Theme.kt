package com.example.watchfinder.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AccentRed,
    onPrimary = LightBeige,
    secondary = AccentRedDark,
    tertiary = Pink80,
    onSecondary = DarkRedTextOnContainer,
    secondaryContainer = DarkRedContainer,
    onSecondaryContainer = AccentRedDark,
    background = DarkBackground,
    onBackground = LightContent,
    surface = DarkBackground,
    onSurface = LightContent,
    outline = LightOutline,
    onSurfaceVariant = LightOutline
)
private val LightColorScheme = lightColorScheme(
    primary = DarkText,
    onPrimary = LightRedContainer,
    secondary = AccentRed,
    onSecondary = WhiteText,
    secondaryContainer = LightRedContainer,
    onSecondaryContainer = DarkRedTextOnContainer,
    background = LightBeige,
    onBackground = DarkText,
    surface = LightBeige,
    onSurface = DarkText,
    outline = MediumGray,
    onSurfaceVariant = MediumGray
)

@Composable
fun WatchFinderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}