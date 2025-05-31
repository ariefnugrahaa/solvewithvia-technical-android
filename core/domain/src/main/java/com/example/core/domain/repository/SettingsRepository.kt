package com.example.core.domain.repository

import com.example.core.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>
    suspend fun updateSettings(appSettings: AppSettings)
} 