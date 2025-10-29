package com.citasmedicas.data.repository

import android.content.Context
import com.citasmedicas.data.local.AppDatabase
import com.citasmedicas.data.local.entity.UserEntity
import com.citasmedicas.data.session.SessionManager
import com.citasmedicas.model.User
import com.citasmedicas.data.mapper.toModel
import com.citasmedicas.util.SecurityUtils
import kotlinx.coroutines.flow.first
import java.util.UUID

class UserRepository(private val db: AppDatabase, private val context: Context) {
    private val userDao = db.userDao()
    private val session by lazy { SessionManager(context) }

    suspend fun register(email: String, password: String, name: String?): Result<Unit> {
        val existing = userDao.getByEmail(email)
        if (existing != null) return Result.failure(IllegalStateException("Email ya registrado"))
        val salt = SecurityUtils.generateSalt()
        val hash = SecurityUtils.hashPassword(password, salt)
        val user = UserEntity(
            id = UUID.randomUUID().toString(),
            email = email.trim(),
            name = name,
            passwordHash = hash,
            salt = salt
        )
        userDao.insert(user)
        return Result.success(Unit)
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        val user = userDao.getByEmail(email.trim()) ?: return Result.failure(IllegalArgumentException("Usuario no encontrado"))
        val hash = SecurityUtils.hashPassword(password, user.salt)
        return if (hash == user.passwordHash) Result.success(user)
        else Result.failure(IllegalArgumentException("Credenciales inválidas"))
    }

    suspend fun getCurrentUser(): User? {
        val userId = session.userIdFlow.first() ?: return null
        return userDao.getById(userId)?.toModel()
    }

    suspend fun updateUser(
        name: String?,
        phone: String?,
        address: String?,
        birthDate: String?
    ): Result<Unit> {
        val userId = session.userIdFlow.first() ?: return Result.failure(IllegalStateException("Usuario no autenticado"))
        val existingUser = userDao.getById(userId) ?: return Result.failure(IllegalStateException("Usuario no encontrado"))
        
        val updatedUser = existingUser.copy(
            name = name?.takeIf { it.isNotBlank() },
            phone = phone?.takeIf { it.isNotBlank() },
            address = address?.takeIf { it.isNotBlank() },
            birthDate = birthDate?.takeIf { it.isNotBlank() }
        )
        
        return try {
            userDao.update(updatedUser)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        session.clear()
    }
}
