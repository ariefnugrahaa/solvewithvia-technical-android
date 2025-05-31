package com.example.core.domain.usecase.settings

import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class SettingsUseCasesTest {

    @Test
    fun `SettingsUseCases can be instantiated and properties accessed`() {
        // Given
        val getAppSettingsUseCase = mockk<GetAppSettingsUseCase>()
        val updateAppSettingsUseCase = mockk<UpdateAppSettingsUseCase>()

        // When
        val settingsUseCases = SettingsUseCases(
            getAppSettings = getAppSettingsUseCase,
            updateAppSettings = updateAppSettingsUseCase
        )

        // Then
        assertNotNull(settingsUseCases.getAppSettings)
        assertNotNull(settingsUseCases.updateAppSettings)
    }
} 