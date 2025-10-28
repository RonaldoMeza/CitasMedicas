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
import com.citasmedicas.ui.screens.notifications.NotificationsScreen
import com.citasmedicas.model.Appointment
import com.citasmedicas.data.repository.AppointmentRepository
import android.util.Log


/**
 * Configuración principal de navegación
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val appointmentRepository = AppointmentRepository()

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
                },
                onNavigateToAppointment = { doctorId ->
                    navController.navigate(Routes.Appointment.createRoute(doctorId))
                },
                onNavigateToNotifications = {
                    navController.navigate(Routes.Notifications.route)
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
                initialSearchQuery = searchQuery,
                onNavigateToNotifications = {
                    navController.navigate(Routes.Notifications.route)
                }
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
                },
                onNavigateToNotifications = {
                    navController.navigate(Routes.Notifications.route)
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
                },
                onNavigateToAppointmentDetail = { appointment ->
                    // Mostrar detalles de la cita en un diálogo o nueva pantalla
                    // Por ahora lo dejamos vacío o podemos crear una pantalla de detalles
                    Log.d("AppNavigation", "Showing appointment detail: ${appointment.id}")
                },
                onNavigateToAppointment = { identifier ->
                    // El identificador puede ser un appointmentId o un doctorId
                    if (identifier.startsWith("appointment_")) {
                        // Es un ID de cita - buscar la cita para obtener el doctorId
                        val appointment = appointmentRepository.getAppointmentById(identifier)
                        appointment?.let {
                            Log.d("AppNavigation", "Reprogramming appointment: ${it.id} for doctor: ${it.doctorId}")
                            navController.navigate(Routes.Appointment.createRoute(it.doctorId, it.id))
                        }
                    } else {
                        // Es un doctorId directamente
                        navController.navigate(Routes.Appointment.createRoute(identifier))
                    }
                },
                onNavigateToSearch = {
                    navController.navigate(Routes.Search.route)
                },
                onNavigateToNotifications = {
                    navController.navigate(Routes.Notifications.route)
                }
            )
        }

        // Pantalla de agendar cita
        composable(
            route = "appointment/{doctorId}/{appointmentId}",
            arguments = listOf(
                androidx.navigation.navArgument("doctorId") {
                    type = androidx.navigation.NavType.StringType
                },
                androidx.navigation.navArgument("appointmentId") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = "null"
                }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            val appointmentId = backStackEntry.arguments?.getString("appointmentId")

            android.util.Log.d("AppNavigation", "Received doctorId: '$doctorId', appointmentId: '$appointmentId'")

            AppointmentScreen(
                appointmentId = if (appointmentId == "null" || appointmentId.isNullOrBlank()) null else appointmentId,
                doctorId = doctorId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAppointmentScheduled = {
                    // Navegar a mis citas después de agendar/reprogramar
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
                },
                onNavigateToNotifications = {
                    navController.navigate(Routes.Notifications.route)
                }
            )
        }

        // Pantalla de notificaciones
        composable(Routes.Notifications.route) {
            NotificationsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAppointmentDetail = { appointment ->
                    // Regresar y navegar a mis citas para ver el detalle
                    navController.popBackStack()
                    // Opcional: podríamos navegar directamente al detalle de la cita
                }
            )
        }
    }
}
