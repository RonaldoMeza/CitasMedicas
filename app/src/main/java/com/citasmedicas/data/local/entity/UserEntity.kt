package com.citasmedicas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val name: String?,
    val phone: String? = null,
    val address: String? = null,
    val birthDate: String? = null,
    val passwordHash: String,
    val salt: String
)
