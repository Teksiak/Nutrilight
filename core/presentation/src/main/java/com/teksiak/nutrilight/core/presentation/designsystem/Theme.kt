package com.teksiak.nutrilight.core.presentation.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme()

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = White,
    outline = ShadedWhite,
    surfaceContainer = AlmostWhite,
    onSurface = TintedBlack,
    onSurfaceVariant = Silver,

)

@Composable
fun NutrilightTheme(
    darkTheme: Boolean = false, // isSystemInDarkTheme()
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}