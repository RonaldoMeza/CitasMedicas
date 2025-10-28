package com.citasmedicas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.citasmedicas.data.local.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {
    @Query("SELECT * FROM doctors")
    fun getAll(): Flow<List<DoctorEntity>>

    // Synchronous helpers
    @Query("SELECT * FROM doctors")
    fun getAllOnce(): List<DoctorEntity>

    @Query("SELECT * FROM doctors WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): DoctorEntity?

    @Query("SELECT * FROM doctors WHERE id = :id LIMIT 1")
    fun getByIdOnce(id: String): DoctorEntity?

    @Query("SELECT * FROM doctors WHERE name LIKE '%' || :q || '%' OR specialty LIKE '%' || :q || '%'")
    fun search(q: String): Flow<List<DoctorEntity>>

    @Query("SELECT * FROM doctors WHERE name LIKE '%' || :q || '%' OR specialty LIKE '%' || :q || '%'")
    fun searchOnce(q: String): List<DoctorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<DoctorEntity>)
}
