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
    
}
