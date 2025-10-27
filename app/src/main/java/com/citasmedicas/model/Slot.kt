package com.citasmedicas.model

/**
 * Modelo de datos para un horario disponible (slot)
 */
data class Slot(
    val id: String,
    val doctorId: String,
    val date: String,           // Fecha en formato DD/MM/YYYY
    val time: String,           // Hora en formato HH:MM
    val isAvailable: Boolean = true,
    val isBooked: Boolean = false
)

