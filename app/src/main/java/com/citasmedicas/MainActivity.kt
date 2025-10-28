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
import com.citasmedicas.navigation.Routes
import com.citasmedicas.ui.components.MediTurnBottomNavigation
import com.citasmedicas.ui.theme.CitasMedicasTheme
import com.citasmedicas.util.AppContextProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppContextProvider.init(applicationContext)
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
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.Home.route
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentRoute != Routes.Login.route) {
                MediTurnBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigateToHome = {
                        navController.navigate(Routes.Home.route) {
                            popUpTo(Routes.Home.route) { inclusive = true }
                        }
                    },
                    onNavigateToSearch = {
                        navController.navigate(Routes.Search.route)
                    },
                    onNavigateToAppointments = {
                        navController.navigate(Routes.MyAppointments.route)
                    },
                    onNavigateToProfile = {
                        navController.navigate(Routes.Profile.route)
                    }
                )
            }
        }
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}