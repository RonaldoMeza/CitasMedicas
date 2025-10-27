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
            name = "Dra. María González",
            specialty = "Cardiología",
            rating = 4.9,
            reviews = 127,
            experience = 15,
            location = "Hospital Central",
            isAvailable = true,
            price = 50,
            schedule = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00"),
            description = "Especialista en enfermedades cardiovasculares con más de 15 años de experiencia. Tratamiento de hipertensión, arritmias y rehabilitación cardiaca.",
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
            description = "Experto en trastornos neurológicos y enfermedades del sistema nervioso. Diagnóstico y tratamiento de migrañas, epilepsia y trastornos del sueño.",
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
            description = "Pediatra con amplia experiencia en cuidado infantil y desarrollo del niño. Atención desde recién nacidos hasta adolescentes.",
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
            description = "Médico generalista con enfoque en prevención y atención primaria. Consultas generales y seguimiento de enfermedades crónicas.",
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
            description = "Especialista en enfermedades de la piel, pelo y uñas. Tratamiento de acné, psoriasis y cirugía dermatológica.",
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
}
