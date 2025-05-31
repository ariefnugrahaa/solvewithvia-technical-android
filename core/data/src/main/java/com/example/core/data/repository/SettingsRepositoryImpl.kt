package com.example.core.data.repository

import android.content.Context
import androidx.core.content.edit
import com.example.core.domain.model.AppSettings
import com.example.core.domain.model.FontSize
import com.example.core.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    private val sharedPreferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    override fun getSettings(): Flow<AppSettings> = flow {
        try {
            emit(
                AppSettings(
                    isDarkMode = sharedPreferences.getBoolean("is_dark_mode", false),
                    fontSize = FontSize.valueOf(
                        sharedPreferences.getString("font_size", FontSize.MEDIUM.name) ?: FontSize.MEDIUM.name
                    )
                )
            )
        } catch (e: Exception) {
            emit(AppSettings(isDarkMode = false, fontSize = FontSize.MEDIUM))
            throw Exception("Failed to load settings: ${e.message}", e)
        }
    }

    override suspend fun updateSettings(appSettings: AppSettings) {
        try {
            sharedPreferences.edit {
                putBoolean("is_dark_mode", appSettings.isDarkMode)
                    .putString("font_size", appSettings.fontSize.name)
            }
        } catch (e: Exception) {
            throw Exception("Failed to save settings: ${e.message}", e)
        }
    }
}

