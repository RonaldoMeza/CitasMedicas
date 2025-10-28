package com.citasmedicas.data

import com.citasmedicas.model.Appointment

/**
 * Estado global de notificaciones
 */
object NotificationState {
    private val readNotifications = mutableSetOf<String>()

    fun markAsRead(appointmentId: String) {
        readNotifications.add(appointmentId)
    }

    fun isRead(appointmentId: String): Boolean {
        return readNotifications.contains(appointmentId)
    }

    fun getUnreadCount(appointments: List<Appointment>): Int {
        return appointments.count { !isRead(it.id) }
    }
}

