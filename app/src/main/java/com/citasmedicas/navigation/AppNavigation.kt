package com.citasmedicas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.citasmedicas.ui.screens.appointment.AppointmentScreen

import com.citasmedicas.ui.screens.doctor.DoctorDetailScreen
import com.citasmedicas.ui.screens.home.HomeScreen
import com.citasmedicas.ui.screens.profile.ProfileScreen


/**
 * Configuración principal de navegación
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        // Pantalla principal
        composable(Routes.Home.route) {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(Routes.Search.route)
                },
                onNavigateToMyAppointments = {
                    navController.navigate(Routes.MyAppointments.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.Profile.route)
                }
            )
        }
        

        
        // Pantalla de detalle del médico
        composable(Routes.DoctorDetail.route) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            DoctorDetailScreen(
                doctorId = doctorId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAppointment = { doctorId ->
                    navController.navigate(Routes.Appointment.createRoute(doctorId))
                }
            )
        }
        
        // Pantalla de agendar cita
        composable(Routes.Appointment.route) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            AppointmentScreen(
                doctorId = doctorId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAppointmentScheduled = {
                    // Navegar a mis citas después de agendar
                    navController.navigate(Routes.MyAppointments.route) {
                        popUpTo(Routes.Home.route) { inclusive = false }
                    }
                }
            )
        }
        

        
        // Pantalla de perfil
        composable(Routes.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToEditProfile = {
                    // Navegación a editar perfil (por implementar)
                }

            )
        }
    }
}
