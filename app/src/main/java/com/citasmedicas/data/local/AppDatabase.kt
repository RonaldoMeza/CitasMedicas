package com.citasmedicas.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.citasmedicas.data.local.converter.Converters
import com.citasmedicas.data.local.dao.*
import com.citasmedicas.data.local.entity.*

@Database(
    entities = [
        UserEntity::class,
        DoctorEntity::class,
        AppointmentEntity::class,
        ReminderEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun doctorDao(): DoctorDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun reminderDao(): ReminderDao
}
