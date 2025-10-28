package com.citasmedicas.navigation

/**
 * Rutas de navegación MediTurn
 */
sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Search : Routes("search") {
        fun createRoute(searchQuery: String? = null) =
            if (searchQuery != null) "search?searchQuery=$searchQuery"
            else "search"
    }
    object DoctorDetail : Routes("doctor_detail/{doctorId}") {
        fun createRoute(doctorId: String) = "doctor_detail/$doctorId"
    }
    object Appointment : Routes("appointment/{doctorId}/{appointmentId}") {
        fun createRoute(doctorId: String, appointmentId: String? = null) =
            if (appointmentId != null) "appointment/$doctorId/$appointmentId"
            else "appointment/$doctorId/null"
    }
    object MyAppointments : Routes("my_appointments")
    object Profile : Routes("profile")
    object Notifications : Routes("notifications")
}

/**
 * Rutas principales
 */
object AppRoutes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val DOCTOR_DETAIL = "doctor_detail"
    const val APPOINTMENT = "appointment"
    const val MY_APPOINTMENTS = "my_appointments"
    const val PROFILE = "profile"
}