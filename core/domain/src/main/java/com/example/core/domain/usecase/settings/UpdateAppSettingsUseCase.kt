package com.example.core.domain.usecase.settings

import com.example.core.domain.model.AppSettings
import com.example.core.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateAppSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(appSettings: AppSettings) = repository.updateSettings(appSettings)
} 