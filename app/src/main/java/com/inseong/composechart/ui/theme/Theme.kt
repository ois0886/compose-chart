package com.inseong.composechart.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8BBCFF),
    onPrimary = Color(0xFF00315F),
    primaryContainer = Color(0xFF164B7F),
    onPrimaryContainer = Color(0xFFD5E7FF),
    secondary = Color(0xFF69D6CB),
    onSecondary = Color(0xFF003733),
    secondaryContainer = Color(0xFF18514C),
    onSecondaryContainer = Color(0xFFB7F0EA),
    tertiary = Color(0xFF7DDC8C),
    onTertiary = Color(0xFF00390D),
    tertiaryContainer = Color(0xFF245130),
    onTertiaryContainer = Color(0xFFC8F3D0),
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutline,
)

private val LightColorScheme = lightColorScheme(
    primary = ChartBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD9E9FF),
    onPrimaryContainer = Color(0xFF0A315E),
    secondary = ChartTeal,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD7F5F2),
    onSecondaryContainer = Color(0xFF073B38),
    tertiary = ChartGreen,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDDF5E4),
    onTertiaryContainer = Color(0xFF143B20),
    background = LightBackground,
    onBackground = LightOnSurface,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutline,
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

@Composable
fun ComposeChartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
        shapes = AppShapes,
        content = content,
    )
}
