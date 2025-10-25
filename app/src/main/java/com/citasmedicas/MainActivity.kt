package com.citasmedicas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.citasmedicas.navigation.AppNavigation
import com.citasmedicas.ui.components.MediTurnBottomNavigation
import com.citasmedicas.ui.theme.CitasMedicasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CitasMedicasTheme {
                MediTurnApp()
            }
        }
    }
}

@Composable
fun MediTurnApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            MediTurnBottomNavigation(
                currentRoute = currentRoute,
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateToSearch = {
                    navController.navigate("search")
                },
                onNavigateToAppointments = {
                    navController.navigate("my_appointments")
                },
                onNavigateToProfile = {
                    navController.navigate("profile")
                }
            )
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}