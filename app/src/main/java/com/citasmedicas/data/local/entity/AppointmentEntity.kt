package com.citasmedicas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val doctorId: String,
    val doctorName: String,
    val specialty: String,
    val dateIso: String,
    val time24: String,
    val reason: String,
    val price: Int
)
