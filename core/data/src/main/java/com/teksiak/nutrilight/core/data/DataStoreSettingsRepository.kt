package com.teksiak.nutrilight.core.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.teksiak.nutrilight.core.domain.Country
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.domain.SettingsRepository.Companion.DATASTORE_NAME
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.EmptyResult
import com.teksiak.nutrilight.core.domain.util.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreSettingsRepository @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
): SettingsRepository {
    private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_NAME
    )

    override val showProductImages: Flow<Boolean>
        get() = applicationContext.settingsDataStore.data
            .catch {
                if(it is Exception) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { preferences ->
                preferences[SHOW_PRODUCT_IMAGES_KEY] ?: true
            }

    override val countryCode: Flow<Country>
        get() = applicationContext.settingsDataStore.data
            .catch {
                if(it is Exception) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { preferences ->
                preferences[COUNTRY_KEY]?.let {
                    Country.fromCode(it)
                } ?: Country.POLAND
                //TODO: Change this to get user country somehow
    }

    override val historySize: Flow<Int>
        get() = applicationContext.settingsDataStore.data
            .catch {
                if(it is Exception) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { preferences ->
                preferences[HISTORY_SIZE_KEY] ?: 10
            }

    override suspend fun toggleShowProductImages(): EmptyResult<DataError.Local> {
        return safeSettingsChange {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[SHOW_PRODUCT_IMAGES_KEY] = !(preferences[SHOW_PRODUCT_IMAGES_KEY] ?: true)
            }
        }
    }

    override suspend fun setCountryCode(countryCode: String): EmptyResult<DataError.Local> {
        return safeSettingsChange {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[COUNTRY_KEY] = countryCode
            }
        }
    }

    override suspend fun setHistorySize(size: Int): EmptyResult<DataError.Local> {
        Log.d("DataStoreSettingsRepository", "setHistorySize: $size")
        return safeSettingsChange {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[HISTORY_SIZE_KEY] = size
            }
        }
    }

    private suspend fun safeSettingsChange(action: suspend () -> Unit): EmptyResult<DataError.Local> {
        return try {
            action()
            Result.Success(Unit)
        } catch (e: IOException) {
            Result.Error(DataError.Local.DATASTORE_ERROR)
        }
    }

    companion object {
        private val SHOW_PRODUCT_IMAGES_KEY = booleanPreferencesKey(SettingsRepository.SHOW_PRODUCT_IMAGES_NAME)
        private val COUNTRY_KEY = stringPreferencesKey(SettingsRepository.COUNTRY_NAME)
        private val HISTORY_SIZE_KEY = intPreferencesKey(SettingsRepository.HISTORY_SIZE)
    }
}