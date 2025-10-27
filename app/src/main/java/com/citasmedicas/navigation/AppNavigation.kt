package com.citasmedicas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.citasmedicas.ui.screens.appointment.AppointmentScreen
import com.citasmedicas.ui.screens.calendar.MyAppointmentsScreen
import com.citasmedicas.ui.screens.doctor.DoctorDetailScreen
import com.citasmedicas.ui.screens.home.HomeScreen
import com.citasmedicas.ui.screens.profile.ProfileScreen
import com.citasmedicas.ui.screens.search.SearchScreen
import android.util.Log


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
                onNavigateToSearch = { specialtyName ->
                    if (specialtyName != null) {
                        navController.navigate(Routes.Search.createRoute(specialtyName))
                    } else {
                        navController.navigate(Routes.Search.route)
                    }
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
        composable(
            route = Routes.DoctorDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("doctorId") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            Log.d("AppNavigation", "Navigating to doctor detail with ID: '$doctorId'")
            DoctorDetailScreen(
                doctorId = doctorId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAppointment = { docId ->
                    navController.navigate(Routes.Appointment.createRoute(docId))
                }
            )
        }

        // Pantalla de búsqueda
        composable(
            route = "search?searchQuery={searchQuery}",
            arguments = listOf(
                androidx.navigation.navArgument("searchQuery") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val searchQuery = backStackEntry.arguments?.getString("searchQuery")
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDoctorDetail = { doctorId ->
                    Log.d("AppNavigation", "Creating route for doctor: $doctorId")
                    val route = Routes.DoctorDetail.createRoute(doctorId)
                    Log.d("AppNavigation", "Navigating to: $route")
                    navController.navigate(route)
                },
                initialSearchQuery = searchQuery
            )
        }
        
        // Ruta alternativa sin parámetros
        composable("search") {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDoctorDetail = { doctorId ->
                    val route = Routes.DoctorDetail.createRoute(doctorId)
                    navController.navigate(route)
                }
            )
        }

        // Pantalla de mis citas
        composable(Routes.MyAppointments.route) {
            MyAppointmentsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDoctorDetail = { doctorId ->
                    Log.d("AppNavigation", "Creating route for doctor: $doctorId")
                    val route = Routes.DoctorDetail.createRoute(doctorId)
                    Log.d("AppNavigation", "Navigating to: $route")
                    navController.navigate(route)
                }
            )
        }

        // Pantalla de agendar cita
        composable(
            route = Routes.Appointment.route,
            arguments = listOf(
                androidx.navigation.navArgument("doctorId") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) { backStackEntry ->
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
