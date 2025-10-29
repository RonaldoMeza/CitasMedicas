package com.citasmedicas.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.citasmedicas.ui.theme.MediTurnBlue

/**
 * Bottom Navigation Bar para MediTurn
 * Segun diseño de nuestro figma
 */
@Composable
fun MediTurnBottomNavigation(
    currentRoute: String,
    onNavigateToHome: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    NavigationBar(
        containerColor = colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        val navItems = listOf(
            NavItem("Inicio", Icons.Default.Home, "home"),
            NavItem("Buscar", Icons.Default.Search, "search"),
            NavItem("Citas", Icons.Default.DateRange, "my_appointments"),
            NavItem("Perfil", Icons.Default.Person, "profile")
        )
        
        navItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) MediTurnBlue else colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp,
                        fontWeight = if (currentRoute == item.route) FontWeight.Bold else FontWeight.Normal,
                        color = if (currentRoute == item.route) MediTurnBlue else colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    when (item.route) {
                        "home" -> onNavigateToHome()
                        "search" -> onNavigateToSearch()
                        "my_appointments" -> onNavigateToAppointments()
                        "profile" -> onNavigateToProfile()
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MediTurnBlue,
                    selectedTextColor = MediTurnBlue,
                    unselectedIconColor = colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = colorScheme.onSurface.copy(alpha = 0.6f),
                    indicatorColor = MediTurnBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)
