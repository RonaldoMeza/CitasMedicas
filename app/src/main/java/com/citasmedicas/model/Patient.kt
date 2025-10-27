package com.citasmedicas.model

/**
 * Modelo de datos para un paciente/usuario
 * Información del paciente que usa la aplicación
 */
data class Patient(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String = "",
    val dateOfBirth: String = "",
    val profileImageUrl: String = ""
)

