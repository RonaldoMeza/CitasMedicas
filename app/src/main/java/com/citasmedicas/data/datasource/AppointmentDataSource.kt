package com.citasmedicas.data.datasource

import com.citasmedicas.model.Appointment
import java.util.UUID

/**
 * DataSource para gestionar citas médicas
 */
object AppointmentDataSource {

    private val appointments = mutableListOf<Appointment>()

    init {
        appointments.addAll(listOf(
            Appointment(
                id = "appointment_1",
                doctorId = "doctor_1",
                doctorName = "Dra. María González",
                specialty = "Cardiología",
                date = "01/12/2025",
                time = "10:00",
                reason = "Consulta de rutina",
                price = 50
            ),
            Appointment(
                id = "appointment_2",
                doctorId = "doctor_2",
                doctorName = "Dr. Carlos Ramírez",
                specialty = "Neurología",
                date = "05/12/2025",
                time = "14:30",
                reason = "Evaluación neurológica",
                price = 60
            )
        ))
    }

    /**
     * Obtener todas las citas del usuario
     */
    fun getAllAppointments(): List<Appointment> {
        return appointments.toList()
    }

    /**
     * Obtener cita por ID
     */
    fun getAppointmentById(id: String): Appointment? {
        return appointments.find { it.id == id }
    }

    /**
     * Obtener citas de un médico específico
     */
    fun getAppointmentsByDoctorId(doctorId: String): List<Appointment> {
        return appointments.filter { it.doctorId == doctorId }
    }

    /**
     * Agregar una nueva cita
     */
    fun addAppointment(appointment: Appointment) {
        appointments.add(appointment)
    }

    /**
     * Actualizar una cita existente
     */
    fun updateAppointment(appointment: Appointment) {
        val index = appointments.indexOfFirst { it.id == appointment.id }
        if (index != -1) {
            appointments[index] = appointment
        }
    }

    /**
     * Eliminar una cita
     */
    fun removeAppointment(appointmentId: String): Boolean {
        return appointments.removeIf { it.id == appointmentId }
    }
}

