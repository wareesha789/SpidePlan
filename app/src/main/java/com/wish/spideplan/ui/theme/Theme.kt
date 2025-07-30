package com.wish.spideplan.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SpiderRed,
    onPrimary = OnPrimaryDark,
    primaryContainer = SpiderRedDark,
    onPrimaryContainer = OnPrimaryDark,
    
    secondary = SpiderBlue,
    onSecondary = OnPrimaryDark,
    secondaryContainer = SpiderBlueDark,
    onSecondaryContainer = OnPrimaryDark,
    
    tertiary = WebSilver,
    onTertiary = BackgroundDark,
    tertiaryContainer = WebGray,
    onTertiaryContainer = OnSurfaceDark,
    
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceDark,
    
    outline = WebGray,
    outlineVariant = WebGrayLight,
    
    error = ErrorColor,
    onError = OnPrimaryDark,
    errorContainer = ErrorColor,
    onErrorContainer = OnPrimaryDark,
    
    inverseSurface = OnSurfaceDark,
    inverseOnSurface = SurfaceDark,
    inversePrimary = SpiderRedLight,
    
    surfaceTint = SpiderRed,
    scrim = BackgroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = SpiderRed,
    onPrimary = OnPrimaryDark,
    primaryContainer = SpiderRedLight,
    onPrimaryContainer = BackgroundDark,
    
    secondary = SpiderBlue,
    onSecondary = OnPrimaryDark,
    secondaryContainer = SpiderBlueLight,
    onSecondaryContainer = BackgroundDark,
    
    tertiary = WebGray,
    onTertiary = OnPrimaryDark,
    tertiaryContainer = WebGrayLight,
    onTertiaryContainer = BackgroundDark,
    
    background = OnBackgroundDark,
    onBackground = BackgroundDark,
    
    surface = OnSurfaceDark,
    onSurface = BackgroundDark,
    surfaceVariant = WebSilver,
    onSurfaceVariant = BackgroundDark,
    
    outline = WebGray,
    outlineVariant = WebGrayLight,
    
    error = ErrorColor,
    onError = OnPrimaryDark,
    errorContainer = ErrorColor,
    onErrorContainer = OnPrimaryDark,
    
    inverseSurface = BackgroundDark,
    inverseOnSurface = OnBackgroundDark,
    inversePrimary = SpiderRed,
    
    surfaceTint = SpiderRed,
    scrim = BackgroundDark
)

@Composable
fun SpidePlanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to maintain Spider-Man theme
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
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}