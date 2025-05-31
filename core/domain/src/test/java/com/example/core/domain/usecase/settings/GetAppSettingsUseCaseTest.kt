package com.example.core.domain.usecase.settings

import com.example.core.domain.model.AppSettings
import com.example.core.domain.model.FontSize
import com.example.core.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAppSettingsUseCaseTest {

    private lateinit var repository: SettingsRepository
    private lateinit var useCase: GetAppSettingsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAppSettingsUseCase(repository)
    }

    @Test
    fun `when invoke is called, should return app settings from repository`() = runBlocking {
        // Given
        val expectedSettings = AppSettings(isDarkMode = true, fontSize = FontSize.MEDIUM)
        coEvery { repository.getSettings() } returns flowOf(expectedSettings)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expectedSettings, result)
    }
} 