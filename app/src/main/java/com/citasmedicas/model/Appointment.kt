package com.citasmedicas.model

/**
 * Modelo de datos para una cita médica
 */
data class Appointment(
    val id: String,
    val doctorId: String,
    val doctorName: String,
    val specialty: String,
    val date: String,
    val time: String,
    val reason: String,
    val price: Int
)
