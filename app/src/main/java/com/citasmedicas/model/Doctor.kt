package com.citasmedicas.model

/**
 * Modelo de datos para un médico
 */
data class Doctor(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviews: Int,
    val experience: Int,
    val location: String,
    val isAvailable: Boolean,
    val price: Int,
    val schedule: List<String> = emptyList(), // Horarios disponibles
    val imageUrl: String = "", // URL de la imagen
    val description: String = "", // Descripción del médico
    val phoneNumber: String = "", // Número de teléfono
    val supportsTelemedicine: Boolean = false // Soporte para teleconsulta
)