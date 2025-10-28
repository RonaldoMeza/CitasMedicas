package com.citasmedicas.data.repository

import com.citasmedicas.model.Doctor
import com.citasmedicas.data.local.DatabaseProvider
import com.citasmedicas.data.mapper.toModel
import com.citasmedicas.util.AppContextProvider

/**
 * Repository que gestiona los datos de médicos
 * Actúa como capa de abstracción entre UI y DataSource
 */
class DoctorRepository {

    private val doctorDao by lazy {
        DatabaseProvider.get(AppContextProvider.context).doctorDao()
    }

    /**
     * Obtener todos los médicos
     
     */
    fun getAllDoctors(): List<Doctor> {
        return doctorDao.getAllOnce().map { it.toModel() }
    }

    /**
     * Buscar médico por ID
     */
    fun getDoctorById(id: String): Doctor? {
        return doctorDao.getByIdOnce(id)?.toModel()
    }

    /**
     * Buscar médicos por especialidad
     */
    fun getDoctorsBySpecialty(specialty: String): List<Doctor> {
        return doctorDao.getAllOnce()
            .map { it.toModel() }
            .filter { it.specialty.contains(specialty, ignoreCase = true) }
    }

    /**
     * Buscar médicos por nombre o especialidad
     */
    fun searchDoctorsByName(query: String): List<Doctor> {
        return doctorDao.searchOnce(query).map { it.toModel() }
    }

    /**
     * Buscar médicos por ubicación
     */
    fun getDoctorsByLocation(location: String): List<Doctor> {
        return doctorDao.getAllOnce()
            .map { it.toModel() }
            .filter { it.location.contains(location, ignoreCase = true) }
    }

    /**
     * Obtener médicos con soporte de teleconsulta
     */
    fun getDoctorsWithTelemedicine(): List<Doctor> {
        return doctorDao.getAllOnce()
            .map { it.toModel() }
            .filter { it.supportsTelemedicine }
    }

    /**
     * Obtener médicos destacados (mejor calificación)
     */
    fun getFeaturedDoctors(): List<Doctor> {
        return doctorDao.getAllOnce()
            .map { it.toModel() }
            .sortedByDescending { it.rating }
            .take(3)
    }

    /**
     * Obtener médicos disponibles (que tienen isAvailable = true)
     */
    fun getAvailableDoctors(): List<Doctor> {
        return doctorDao.getAllOnce()
            .map { it.toModel() }
            .filter { it.isAvailable }
    }
}
