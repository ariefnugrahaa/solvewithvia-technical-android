package com.example.core.domain.usecase.settings

import javax.inject.Inject

data class SettingsUseCases @Inject constructor(
    val getAppSettings: GetAppSettingsUseCase,
    val updateAppSettings: UpdateAppSettingsUseCase
) 