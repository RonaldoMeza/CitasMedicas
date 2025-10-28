package com.citasmedicas.data.repository

import com.citasmedicas.model.Appointment
import com.citasmedicas.data.datasource.AppointmentDataSource
import com.citasmedicas.data.datasource.DoctorDataSource
import java.text.SimpleDateFormat
import java.util.*

/**
 * Repository para gestionar las citas médicas
 */
class AppointmentRepository(
    private val appointmentDataSource: AppointmentDataSource = AppointmentDataSource,
    private val doctorDataSource: DoctorDataSource = DoctorDataSource()
) {

    /**
     * Obtener todas las citas del usuario
     */
    fun getAllAppointments(): List<Appointment> {
        return appointmentDataSource.getAllAppointments()
    }

    /**
     * Obtener citas próximas (futuras)
     */
    fun getUpcomingAppointments(): List<Appointment> {
        val today = getCurrentDateString()
        return appointmentDataSource.getAllAppointments().filter {
            compareDates(it.date, today) >= 0
        }.sortedBy {
            "${it.date} ${it.time}"
        }
    }

    /**
     * Obtener citas pasadas
     */
    fun getPastAppointments(): List<Appointment> {
        val today = getCurrentDateString()
        return appointmentDataSource.getAllAppointments().filter {
            compareDates(it.date, today) < 0
        }
    }

    /**
     * Obtener cita por ID
     */
    fun getAppointmentById(id: String): Appointment? {
        return appointmentDataSource.getAppointmentById(id)
    }

    /**
     * Crear una nueva cita
     */
    fun createAppointment(appointment: Appointment): Boolean {
        return try {
            appointmentDataSource.addAppointment(appointment)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Actualizar una cita existente
     */
    fun updateAppointment(appointment: Appointment): Boolean {
        return try {
            appointmentDataSource.updateAppointment(appointment)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Eliminar una cita
     */
    fun cancelAppointment(appointmentId: String): Boolean {
        return try {
            appointmentDataSource.removeAppointment(appointmentId)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Obtener la fecha actual en formato DD/MM/YYYY
     */
    private fun getCurrentDateString(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    /**
     * Comparar dos fechas en formato DD/MM/YYYY
     * Retorna: negativo si date1 < date2, cero si iguales, positivo si date1 > date2
     */
    private fun compareDates(date1: String, date2: String): Int {
        return try {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val d1 = formatter.parse(date1)
            val d2 = formatter.parse(date2)
            when {
                d1 != null && d2 != null -> d1.compareTo(d2)
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }
}

