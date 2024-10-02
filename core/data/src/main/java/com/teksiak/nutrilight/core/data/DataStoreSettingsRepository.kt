package com.teksiak.nutrilight.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
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
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreSettingsRepository @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
): SettingsRepository {
    private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_NAME
    )

    override fun getShowProductImages(): Flow<Boolean> {
        return applicationContext.settingsDataStore.data
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
    }

    override fun getCountryCode(): Flow<Country> {
        return applicationContext.settingsDataStore.data
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
    }

    override suspend fun toggleShowProductImages(): EmptyResult<DataError.Local> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[SHOW_PRODUCT_IMAGES_KEY] = !(preferences[SHOW_PRODUCT_IMAGES_KEY] ?: true)
            }
            Result.Success(Unit)
        } catch (e: IOException) {
            Result.Error(DataError.Local.DATASTORE_ERROR)
        }
    }

    override suspend fun setCountryCode(countryCode: String): EmptyResult<DataError.Local> {
        return try {
            applicationContext.settingsDataStore.edit { preferences ->
                preferences[COUNTRY_KEY] = countryCode
            }
            Result.Success(Unit)
        } catch (e: IOException) {
            Result.Error(DataError.Local.DATASTORE_ERROR)
        }
    }

    companion object {
        private val SHOW_PRODUCT_IMAGES_KEY = booleanPreferencesKey(SettingsRepository.SHOW_PRODUCT_IMAGES_NAME)
        private val COUNTRY_KEY = stringPreferencesKey(SettingsRepository.COUNTRY_NAME)
    }
}