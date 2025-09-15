package com.example.streamchatdemo.service

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val USE_FAKE = booleanPreferencesKey("use_fake_service")
        val LAST_NAME = stringPreferencesKey("last_greeting_name")
    }

    /** whether to use FakeGreetingService */
    val useFakeFlow: Flow<Boolean> =
        dataStore.data.map { it[Keys.USE_FAKE] ?: false }

    /** last typed name on Greeting screen */
    val lastNameFlow: Flow<String> =
        dataStore.data.map { it[Keys.LAST_NAME] ?: "world" }

    suspend fun setUseFake(value: Boolean) {
        dataStore.edit { it[Keys.USE_FAKE] = value }
    }

    suspend fun setLastName(name: String) {
        dataStore.edit { it[Keys.LAST_NAME] = name }
    }
}
