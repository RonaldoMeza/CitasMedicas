package com.citasmedicas.data.local

import android.content.Context
import androidx.room.Room
import com.citasmedicas.data.local.entity.DoctorEntity
import com.citasmedicas.data.local.dao.DoctorDao
import com.citasmedicas.util.Constans
import com.citasmedicas.data.datasource.DoctorDataSource
import com.citasmedicas.data.mapper.toEntity
import java.util.concurrent.Executors

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun get(context: Context): AppDatabase {
        val instance = INSTANCE ?: synchronized(this) {
            val created = INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            created
        }
        // Seed en background si es necesario
        Executors.newSingleThreadExecutor().execute {
            seedIfNeeded(instance)
        }
        return instance
    }

    private fun buildDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constans.DB_NAME
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun seedIfNeeded(db: AppDatabase) {
        val doctorDao = db.doctorDao()
        val hasDoctors = try { doctorDao.getAllOnce().isNotEmpty() } catch (_: Exception) { true }
        if (!hasDoctors) {
            seedDoctors(doctorDao)
        }
    }

    private fun seedDoctors(doctorDao: DoctorDao) {
        val source = DoctorDataSource()
        val doctors = source.getAllDoctors().map { it.toEntity() }
        try {
            // REPLACE para idempotencia si se vuelve a ejecutar en tests
            doctorDao.insertAll(doctors)
        } catch (_: Exception) {}
    }
}
