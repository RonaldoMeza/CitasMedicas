package com.citasmedicas.data.repository

import com.citasmedicas.model.Appointment
import com.citasmedicas.data.local.DatabaseProvider
import com.citasmedicas.util.AppContextProvider
import com.citasmedicas.util.DateUtils
import com.citasmedicas.data.session.SessionManager
import com.citasmedicas.data.mapper.toEntity
import com.citasmedicas.data.mapper.toModel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

/**
 * Repository para gestionar las citas médicas
 */
class AppointmentRepository {

    private val context get() = AppContextProvider.context
    private val db by lazy { DatabaseProvider.get(context) }
    private val appointmentDao by lazy { db.appointmentDao() }
    private val session by lazy { SessionManager(context) }

    private fun currentUserId(): String? = runBlocking {
        session.userIdFlow.first()
    }

    /**
     * Obtener todas las citas del usuario
     */
    fun getAllAppointments(): List<Appointment> {
        val uid = currentUserId() ?: return emptyList()
        return appointmentDao.getAllOnce(uid).map { it.toModel() }
    }

    /**
     * Obtener citas próximas (futuras)
     */
    fun getUpcomingAppointments(): List<Appointment> {
        val uid = currentUserId() ?: return emptyList()
        val todayIso = DateUtils.todayIso()
        return appointmentDao.getUpcomingOnce(uid, todayIso).map { it.toModel() }
    }

    /**
     * Obtener citas pasadas
     */
    fun getPastAppointments(): List<Appointment> {
        val uid = currentUserId() ?: return emptyList()
        val todayIso = DateUtils.todayIso()
        return appointmentDao.getPastOnce(uid, todayIso).map { it.toModel() }
    }

    /**
     * Obtener cita por ID
     */
    fun getAppointmentById(id: String): Appointment? {
        return runBlocking { appointmentDao.getById(id) }?.toModel()
    }

    /**
     * Crear una nueva cita
     */
    fun createAppointment(appointment: Appointment): Boolean {
        val uid = currentUserId() ?: return false
        return try {
            runBlocking { appointmentDao.insert(appointment.toEntity(uid)) }
            true
        } catch (_: Exception) { false }
    }

    /**
     * Actualizar una cita existente
     */
    fun updateAppointment(appointment: Appointment): Boolean {
        val uid = currentUserId() ?: return false
        return try {
            runBlocking { appointmentDao.update(appointment.toEntity(uid)) }
            true
        } catch (_: Exception) { false }
    }

    /**
     * Eliminar una cita
     */
    fun cancelAppointment(appointmentId: String): Boolean {
        return try {
            runBlocking { appointmentDao.deleteById(appointmentId) }
            true
        } catch (_: Exception) { false }
    }
}

