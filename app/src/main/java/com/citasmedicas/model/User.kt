package com.citasmedicas.model

/**
 * Modelo de dominio para Usuario
 */
data class User(
    val id: String,
    val email: String,
    val name: String?,
    val phone: String? = null,
    val address: String? = null,
    val birthDate: String? = null
)
