package com.citasmedicas.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Colores adaptativos que se ajustan automáticamente según el tema (claro/oscuro)
 */
object AdaptiveColors {
    @Composable
    fun background(): Color = MaterialTheme.colorScheme.surface
    
    @Composable
    fun onBackground(): Color = MaterialTheme.colorScheme.onSurface
    
    @Composable
    fun surface(): Color = MaterialTheme.colorScheme.surface
    
    @Composable
    fun onSurface(): Color = MaterialTheme.colorScheme.onSurface
    
    @Composable
    fun cardBackground(): Color = MaterialTheme.colorScheme.surface
    
    @Composable
    fun textPrimary(): Color = MaterialTheme.colorScheme.onSurface
    
    @Composable
    fun textSecondary(): Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    
    @Composable
    fun textTertiary(): Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
}

/**
 * Extensiones para acceder fácilmente a colores adaptativos
 */
@Composable
fun MaterialTheme.getTextPrimary() = AdaptiveColors.textPrimary()

@Composable
fun MaterialTheme.getTextSecondary() = AdaptiveColors.textSecondary()

@Composable
fun MaterialTheme.getTextTertiary() = AdaptiveColors.textTertiary()

@Composable
fun MaterialTheme.getCardBackground() = AdaptiveColors.cardBackground()

