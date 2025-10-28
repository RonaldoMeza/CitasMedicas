package com.citasmedicas.util

import java.security.MessageDigest
import java.security.SecureRandom
import android.util.Base64

object SecurityUtils {
    private val secureRandom = SecureRandom()

    fun generateSalt(bytes: Int = 16): String {
        val salt = ByteArray(bytes)
        secureRandom.nextBytes(salt)
        return Base64.encodeToString(salt, Base64.NO_WRAP)
    }

    fun sha256(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun hashPassword(password: String, salt: String): String = sha256(password + salt)
}
