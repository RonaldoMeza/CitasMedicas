package com.citasmedicas.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Clases de tamaño de ventana para diseño responsivo
 */
enum class WindowSizeClass {
    Compact,    // Teléfonos en portrait
    Medium,     // Tablets pequeñas / teléfonos grandes en landscape
    Expanded    // Tablets / pantallas grandes
}

/**
 * Información del tamaño de ventana actual
 */
data class WindowSize(
    val widthSizeClass: WindowSizeClass,
    val heightSizeClass: WindowSizeClass,
    val width: Dp,
    val height: Dp
)

/**
 * Calcular la clase de tamaño basada en el ancho en dp
 */
private fun getWindowSizeClass(widthDp: Dp): WindowSizeClass {
    return when {
        widthDp < 600.dp -> WindowSizeClass.Compact
        widthDp < 840.dp -> WindowSizeClass.Medium
        else -> WindowSizeClass.Expanded
    }
}

/**
 * Obtener el tamaño de ventana actual
 */
@Composable
fun MinisterWindowSize(): WindowSize {
    val configuration = LocalConfiguration.current
    val widthDp = configuration.screenWidthDp.dp
    val heightDp = configuration.screenHeightDp.dp
    
    return WindowSize(
        widthSizeClass = getWindowSizeClass(widthDp),
        heightSizeClass = getWindowSizeClass(heightDp),
        width = widthDp,
        height = heightDp
    )
}

/**
 * Verificar si la pantalla es lo suficientemente grande para un layout en dos columnas
 */
@Composable
fun isCompactWidth(): Boolean {
    val windowSize = MinisterWindowSize()
    return windowSize.widthSizeClass == WindowSizeClass.Compact
}

/**
 * Verificar si la pantalla es una tablet o pantalla grande
 */
@Composable
fun isExpandedWidth(): Boolean {
    val windowSize = MinisterWindowSize()
    return windowSize.widthSizeClass == WindowSizeClass.Expanded
}

/**
 * Obtener el número de columnas según el tamaño de pantalla
 */
@Composable
fun getColumnCount(): Int {
    return when (MinisterWindowSize().widthSizeClass) {
        WindowSizeClass.Compact -> 1
        WindowSizeClass.Medium -> 2
        WindowSizeClass.Expanded -> 3
    }
}

