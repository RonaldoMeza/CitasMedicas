package com.citasmedicas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val appointmentId: String,
    val triggerAtMillis: Long,
    val enabled: Boolean
)
