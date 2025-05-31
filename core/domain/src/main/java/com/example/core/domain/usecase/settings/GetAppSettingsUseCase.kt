package com.example.core.domain.usecase.settings

import com.example.core.domain.model.AppSettings
import com.example.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<AppSettings> = repository.getSettings()
} 