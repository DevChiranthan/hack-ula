// In app/src/main/java/com/runanywhere/startup_hackathon20/ui/theme/Theme.kt

package com.runanywhere.startup_hackathon20.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// This defines your app's color palette
private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF533483),
    secondary = androidx.compose.ui.graphics.Color(0xFFE94560),
    background = androidx.compose.ui.graphics.Color(0xFF1A1A2E),
    surface = androidx.compose.ui.graphics.Color(0xFF0F3460),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White
)

@Composable
fun RunAnywhereAITheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // Force dark theme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}