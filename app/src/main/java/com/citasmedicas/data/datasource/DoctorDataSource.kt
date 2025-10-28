package com.citasmedicas.data.datasource

import com.citasmedicas.model.Doctor

/**
 * DataSource para obtener datos de médicos
 */
class DoctorDataSource {

    // Datos simulados
    private val doctors = listOf(
        Doctor(
            id = "doctor_1",
            name = "Dra. Mariana González",
            specialty = "Cardiología",
            rating = 4.9,
            reviews = 127,
            experience = 15,
            location = "Hospital Central",
            isAvailable = true,
            price = 50,
            schedule = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00"),
            description = "Cardióloga con 15 años de experiencia en diagnóstico y manejo de enfermedades cardiovasculares. Manejo de hipertensión, arritmias, ecocardiografía y rehabilitación cardiaca.",
            phoneNumber = "+51 987 654 321"
        ),
        Doctor(
            id = "doctor_2",
            name = "Dr. Carlos Ramírez",
            specialty = "Neurología",
            rating = 4.8,
            reviews = 98,
            experience = 12,
            location = "Clínica San Rafael",
            isAvailable = true,
            price = 60,
            schedule = listOf("08:00", "10:00", "11:00", "13:00", "14:00", "16:00"),
            description = "Neurólogo especialista en migraña, epilepsia y trastornos del sueño. Experiencia en consultas ambulatorias y estudios diagnósticos (EEG, valoración neuroclínica).",
            phoneNumber = "+51 987 654 322"
        ),
        Doctor(
            id = "doctor_3",
            name = "Dra. Ana Martínez",
            specialty = "Pediatría",
            rating = 5.0,
            reviews = 203,
            experience = 20,
            location = "Hospital Infantil",
            isAvailable = false,
            price = 45,
            schedule = listOf("08:00", "09:00", "10:00", "14:00", "15:00", "16:00"),
            description = "Pediatra con amplia trayectoria en seguimiento del crecimiento y desarrollo, vacunas y manejo de patologías agudas y crónicas en niños y adolescentes.",
            phoneNumber = "+51 987 654 323"
        ),
        Doctor(
            id = "doctor_4",
            name = "Dr. Juan Pérez",
            specialty = "Medicina General",
            rating = 4.7,
            reviews = 156,
            experience = 10,
            location = "Clínica del Sol",
            isAvailable = true,
            price = 40,
            schedule = listOf("09:00", "10:00", "11:00", "14:00", "15:00"),
            description = "Médico general con enfoque preventivo y manejo integral de atención primaria. Seguimiento de enfermedades crónicas, control de factores de riesgo y derivación especializada cuando corresponde.",
            phoneNumber = "+51 987 654 324"
        ),
        Doctor(
            id = "doctor_5",
            name = "Dra. Laura Sánchez",
            specialty = "Dermatología",
            rating = 4.9,
            reviews = 134,
            experience = 14,
            location = "Clínica Dermatológica",
            isAvailable = true,
            price = 55,
            schedule = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00"),
            description = "Dermatóloga con experiencia en diagnóstico y tratamiento de enfermedades de la piel, pelo y uñas. Manejo de acné, dermatitis, psoriasis y procedimientos menores dermatológicos.",
            phoneNumber = "+51 987 654 325"
        )
    )


    /**
     * Obtener todos los médicos
     */
    fun getAllDoctors(): List<Doctor> {
        return doctors
    }

    /**
     * Buscar médico por ID
     */
    fun getDoctorById(id: String): Doctor? {
        return doctors.find { it.id == id }
    }

    /**
     * Buscar médicos por especialidad
     */
    fun getDoctorsBySpecialty(specialty: String): List<Doctor> {
        return doctors.filter {
            it.specialty.contains(specialty, ignoreCase = true)
        }
    }

    /**
     * Buscar médicos por nombre
     */
    fun searchDoctorsByName(query: String): List<Doctor> {
        return doctors.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.specialty.contains(query, ignoreCase = true)
        }
    }

    /**
     * Buscar médicos por ubicación
     */
    fun getDoctorsByLocation(location: String): List<Doctor> {
        return doctors.filter {
            it.location.contains(location, ignoreCase = true)
        }
    }

    /**
     * Filtrar médicos que soporten teleconsulta
     */
    fun getDoctorsWithTelemedicine(): List<Doctor> {
        return doctors.filter { it.supportsTelemedicine }
    }
}
