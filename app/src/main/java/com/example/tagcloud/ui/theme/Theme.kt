package com.example.tagcloud.ui.theme

import android.graphics.fonts.FontFamily
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font


private val DarkColorPalette = darkColors(
    primary = Color(0, 119, 255),
    primaryVariant = Color.White,
    secondary = Color.White,
    onSurface = Color(32, 32, 32, 255),
    background = Color(32, 32, 32, 255),
    onBackground = Color(37, 37, 37, 255),

)

private val LightColorPalette = lightColors(
    onSurface= Color(0, 119, 255),
    onBackground = Color.White,
    primary = Color(0, 119, 255),
    primaryVariant = Color(0, 119, 255),
    secondary = Color.White,


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)
@Composable
fun TagCloudTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}