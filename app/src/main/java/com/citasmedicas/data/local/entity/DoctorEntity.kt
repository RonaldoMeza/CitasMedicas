package com.citasmedicas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class DoctorEntity(
    @PrimaryKey val id: String,
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviews: Int,
    val experience: Int,
    val location: String,
    val isAvailable: Boolean,
    val price: Int,
    val schedule: List<String>,
    val imageUrl: String,
    val description: String,
    val phoneNumber: String,
    val supportsTelemedicine: Boolean
)
