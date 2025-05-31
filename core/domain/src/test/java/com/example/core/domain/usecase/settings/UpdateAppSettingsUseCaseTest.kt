package com.example.core.domain.usecase.settings

import com.example.core.domain.model.AppSettings
import com.example.core.domain.model.FontSize
import com.example.core.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UpdateAppSettingsUseCaseTest {

    private lateinit var repository: SettingsRepository
    private lateinit var useCase: UpdateAppSettingsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpdateAppSettingsUseCase(repository)
    }

    @Test
    fun `when invoke is called, should call updateSettings on repository with correct settings`() = runBlocking {
        // Given
        val testAppSettings = AppSettings(isDarkMode = false, fontSize = FontSize.LARGE)
        coEvery { repository.updateSettings(any()) } just Runs

        // When
        useCase(testAppSettings)

        // Then
        coVerify(exactly = 1) { repository.updateSettings(testAppSettings) }
    }
} 