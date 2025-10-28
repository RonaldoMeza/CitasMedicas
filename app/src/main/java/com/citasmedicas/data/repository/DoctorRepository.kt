package com.citasmedicas.data.repository

import com.citasmedicas.data.datasource.DoctorDataSource
import com.citasmedicas.model.Doctor

/**
 * Repository que gestiona los datos de médicos
 * Actúa como capa de abstracción entre UI y DataSource
 */
class DoctorRepository(
    private val dataSource: DoctorDataSource = DoctorDataSource()
) {

    /**
     * Obtener todos los médicos
     */
    fun getAllDoctors(): List<Doctor> {
        return dataSource.getAllDoctors()
    }

    /**
     * Buscar médico por ID
     */
    fun getDoctorById(id: String): Doctor? {
        return dataSource.getDoctorById(id)
    }

    /**
     * Buscar médicos por especialidad
     */
    fun getDoctorsBySpecialty(specialty: String): List<Doctor> {
        return dataSource.getDoctorsBySpecialty(specialty)
    }

    /**
     * Buscar médicos por nombre o especialidad
     */
    fun searchDoctorsByName(query: String): List<Doctor> {
        return dataSource.searchDoctorsByName(query)
    }

    /**
     * Buscar médicos por ubicación
     */
    fun getDoctorsByLocation(location: String): List<Doctor> {
        return dataSource.getDoctorsByLocation(location)
    }

    /**
     * Obtener médicos con soporte de teleconsulta
     */
    fun getDoctorsWithTelemedicine(): List<Doctor> {
        return dataSource.getDoctorsWithTelemedicine()
    }

    /**
     * Obtener médicos destacados (mejor calificación)
     */
    fun getFeaturedDoctors(): List<Doctor> {
        return dataSource.getAllDoctors()
            .sortedByDescending { it.rating }
            .take(3)
    }

    /**
     * Obtener médicos disponibles (que tienen isAvailable = true)
     */
    fun getAvailableDoctors(): List<Doctor> {
        return dataSource.getAllDoctors()
            .filter { it.isAvailable }
            .take(3)
    }
}
