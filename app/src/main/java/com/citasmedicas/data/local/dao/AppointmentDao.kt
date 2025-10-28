package com.citasmedicas.data.local.dao

import androidx.room.*
import com.citasmedicas.data.local.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY dateIso, time24")
    fun getAll(userId: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY dateIso, time24")
    fun getAllOnce(userId: String): List<AppointmentEntity>

    @Query("SELECT * FROM appointments WHERE userId = :userId AND dateIso >= :todayIso ORDER BY dateIso, time24")
    fun getUpcoming(userId: String, todayIso: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE userId = :userId AND dateIso >= :todayIso ORDER BY dateIso, time24")
    fun getUpcomingOnce(userId: String, todayIso: String): List<AppointmentEntity>

    @Query("SELECT * FROM appointments WHERE userId = :userId AND dateIso < :todayIso ORDER BY dateIso DESC, time24 DESC")
    fun getPast(userId: String, todayIso: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE userId = :userId AND dateIso < :todayIso ORDER BY dateIso DESC, time24 DESC")
    fun getPastOnce(userId: String, todayIso: String): List<AppointmentEntity>

    @Query("SELECT * FROM appointments WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): AppointmentEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(item: AppointmentEntity)

    @Update
    suspend fun update(item: AppointmentEntity)

    @Query("DELETE FROM appointments WHERE id = :id")
    suspend fun deleteById(id: String)
}
