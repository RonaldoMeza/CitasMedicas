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
            location = "Lima",
            isAvailable = true,
            price = 50,
            schedule = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00"),
            description = "Cardióloga con 15 años de experiencia en diagnóstico y manejo de enfermedades cardiovasculares. Manejo de hipertensión, arritmias, ecocardiografía y rehabilitación cardiaca.",
            phoneNumber = "+51 987 654 321",
            supportsTelemedicine = false
        ),
        Doctor(
            id = "doctor_2",
            name = "Dr. Carlos Ramírez",
            specialty = "Neurología",
            rating = 4.8,
            reviews = 98,
            experience = 12,
            location = "Arequipa",
            isAvailable = true,
            price = 60,
            schedule = listOf("08:00", "10:00", "11:00", "13:00", "14:00", "16:00"),
            description = "Neurólogo especialista en migraña, epilepsia y trastornos del sueño. Experiencia en consultas ambulatorias y estudios diagnósticos (EEG, valoración neuroclínica).",
            phoneNumber = "+51 987 654 322",
            supportsTelemedicine = false
        ),
        Doctor(
            id = "doctor_3",
            name = "Dra. Ana Martínez",
            specialty = "Pediatría",
            rating = 5.0,
            reviews = 203,
            experience = 20,
            location = "Lima",
            isAvailable = false,
            price = 45,
            schedule = listOf("08:00", "09:00", "10:00", "14:00", "15:00", "16:00"),
            description = "Pediatra con amplia trayectoria en seguimiento del crecimiento y desarrollo, vacunas y manejo de patologías agudas y crónicas en niños y adolescentes.",
            phoneNumber = "+51 987 654 323",
            supportsTelemedicine = false
        ),
        Doctor(
            id = "doctor_4",
            name = "Dr. Juan Pérez",
            specialty = "Medicina General",
            rating = 4.7,
            reviews = 156,
            experience = 10,
            location = "Cusco",
            isAvailable = true,
            price = 40,
            schedule = listOf("09:00", "10:00", "11:00", "14:00", "15:00"),
            description = "Médico general con enfoque preventivo y manejo integral de atención primaria. Seguimiento de enfermedades crónicas, control de factores de riesgo y derivación especializada cuando corresponde.",
            phoneNumber = "+51 987 654 324",
            supportsTelemedicine = true
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
            phoneNumber = "+51 987 654 325",
            supportsTelemedicine = true
        ),
        Doctor(
            id = "doctor_6",
            name = "Dr. Roberto Torres",
            specialty = "Traumatología",
            rating = 4.8,
            reviews = 145,
            experience = 18,
            location = "Trujillo",
            isAvailable = true,
            price = 65,
            schedule = listOf("08:00", "09:00", "10:00", "15:00", "16:00", "17:00"),
            description = "Traumatólogo especialista en cirugía ortopédica, fracturas, lesiones deportivas y artroscopia. Manejo integral de patologías musculoesqueléticas.",
            phoneNumber = "+51 987 654 326",
            supportsTelemedicine = false
        ),
        Doctor(
            id = "doctor_7",
            name = "Dra. Patricia Vargas",
            specialty = "Oftalmología",
            rating = 4.9,
            reviews = 178,
            experience = 16,
            location = "Lima",
            isAvailable = true,
            price = 70,
            schedule = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00"),
            description = "Oftalmóloga con experiencia en cirugía refractiva, catarata, glaucoma y enfermedades de la retina. Consultas de valoración visual completa.",
            phoneNumber = "+51 987 654 327",
            supportsTelemedicine = true
        ),
        Doctor(
            id = "doctor_8",
            name = "Dr. Miguel Ángel Rojas",
            specialty = "Cardiología",
            rating = 4.7,
            reviews = 112,
            experience = 13,
            location = "Arequipa",
            isAvailable = true,
            price = 55,
            schedule = listOf("08:00", "09:00", "10:00", "13:00", "14:00", "15:00"),
            description = "Cardiólogo intervencionista con experiencia en cateterismo cardiaco, angioplastia y manejo de insuficiencia cardiaca.",
            phoneNumber = "+51 987 654 328",
            supportsTelemedicine = true
        ),
        Doctor(
            id = "doctor_9",
            name = "Dra. Sofía Mendoza",
            specialty = "Neurología",
            rating = 4.9,
            reviews = 167,
            experience = 19,
            location = "Lima",
            isAvailable = true,
            price = 75,
            schedule = listOf("09:00", "10:00", "11:00", "15:00", "16:00", "17:00"),
            description = "Neuróloga especialista en enfermedades neurodegenerativas, esclerosis múltiple, Parkinson y Alzheimer. Consultas de valoración cognitiva.",
            phoneNumber = "+51 987 654 329",
            supportsTelemedicine = true
        ),
        Doctor(
            id = "doctor_10",
            name = "Dr. Fernando Castro",
            specialty = "Pediatría",
            rating = 4.8,
            reviews = 189,
            experience = 17,
            location = "Cusco",
            isAvailable = true,
            price = 50,
            schedule = listOf("08:00", "09:00", "10:00", "14:00", "15:00", "16:00"),
            description = "Pediatra con experiencia en neonatología, seguimiento del desarrollo infantil y manejo de enfermedades respiratorias en niños.",
            phoneNumber = "+51 987 654 330",
            supportsTelemedicine = true
        ),
        Doctor(
            id = "doctor_11",
            name = "Dra. Carmen Flores",
            specialty = "Traumatología",
            rating = 4.6,
            reviews = 98,
            experience = 11,
            location = "Lima",
            isAvailable = true,
            price = 60,
            schedule = listOf("09:00", "10:00", "11:00", "14:00", "15:00"),
            description = "Traumatóloga especialista en cirugía de columna, manejo de dolor lumbar y cervical. Tratamiento conservador y quirúrgico.",
            phoneNumber = "+51 987 654 331",
            supportsTelemedicine = false
        ),
        Doctor(
            id = "doctor_12",
            name = "Dr. Ricardo Herrera",
            specialty = "Oftalmología",
            rating = 4.7,
            reviews = 143,
            experience = 14,
            location = "Trujillo",
            isAvailable = true,
            price = 65,
            schedule = listOf("08:00", "09:00", "10:00", "13:00", "14:00", "15:00"),
            description = "Oftalmólogo especialista en cirugía de catarata, corrección de defectos refractivos y manejo de enfermedades de la córnea.",
            phoneNumber = "+51 987 654 332",
            supportsTelemedicine = false
        ),
        Doctor(
            id = "doctor_13",
            name = "Dra. Valeria Jiménez",
            specialty = "Medicina General",
            rating = 4.8,
            reviews = 201,
            experience = 12,
            location = "Arequipa",
            isAvailable = true,
            price = 35,
            schedule = listOf("08:00", "09:00", "10:00", "14:00", "15:00", "16:00"),
            description = "Médico general con enfoque en medicina preventiva, control de enfermedades crónicas y atención integral de adultos.",
            phoneNumber = "+51 987 654 333",
            supportsTelemedicine = true
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
