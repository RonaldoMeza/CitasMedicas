package com.citasmedicas.data.repository

import com.citasmedicas.model.Slot
import com.citasmedicas.model.Doctor
import com.citasmedicas.data.datasource.DoctorDataSource
import java.text.SimpleDateFormat
import java.util.*

/**
 * Repository para gestionar horarios disponibles de médicos
 */
class SlotRepository(
    private val doctorDataSource: DoctorDataSource = DoctorDataSource()
) {

    /**
     * Obtener horarios disponibles para un médico en una fecha específica
     */
    fun getAvailableSlots(doctorId: String, date: String): List<Slot> {
        val doctor = doctorDataSource.getDoctorById(doctorId) ?: return emptyList()
        val today = getCurrentDateString()

        // Generar slots basados en el horario del médico
        val slots = mutableListOf<Slot>()
        doctor.schedule.forEachIndexed { index, time ->
            val slot = Slot(
                id = "${doctorId}_${date}_${time}",
                doctorId = doctorId,
                date = date,
                time = time,
                isAvailable = date >= today, // Solo fechas futuras
                isBooked = false
            )
            slots.add(slot)
        }

        return slots
    }

    /**
     * Obtener la fecha actual en formato DD/MM/YYYY
     */
    private fun getCurrentDateString(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    /**
     * Verificar si un slot está disponible
     */
    fun isSlotAvailable(doctorId: String, date: String, time: String): Boolean {
        val slots = getAvailableSlots(doctorId, date)
        return slots.any {
            it.date == date && it.time == time && it.isAvailable && !it.isBooked
        }
    }

    /**
     * Reservar un slot (marcar como reservado)
     * En un caso real, esto actualizaría la base de datos
     */
    fun reserveSlot(doctorId: String, date: String, time: String): Boolean {
        return if (isSlotAvailable(doctorId, date, time)) {
            true
        } else {
            false
        }
    }
}

