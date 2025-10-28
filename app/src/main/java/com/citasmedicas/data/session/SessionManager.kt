package com.citasmedicas.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.citasmedicas.util.Constans
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = Constans.DATASTORE_NAME)

class SessionManager(private val context: Context) {
    private val KEY_USER_ID = stringPreferencesKey(Constans.PREF_USER_ID)
    private val KEY_LOGGED_IN = booleanPreferencesKey(Constans.PREF_LOGGED_IN)

    val userIdFlow: Flow<String?> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it[KEY_USER_ID] }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { it[KEY_LOGGED_IN] ?: false }

    suspend fun setLoggedIn(userId: String) {
        context.dataStore.edit {
            it[KEY_USER_ID] = userId
            it[KEY_LOGGED_IN] = true
        }
    }

    suspend fun clear() {
        context.dataStore.edit {
            it.remove(KEY_USER_ID)
            it[KEY_LOGGED_IN] = false
        }
    }
}
