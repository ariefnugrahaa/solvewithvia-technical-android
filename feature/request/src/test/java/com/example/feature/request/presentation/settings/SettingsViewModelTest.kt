package com.example.feature.request.presentation.settings

import com.example.core.domain.model.AppSettings
import com.example.core.domain.model.FontSize
import com.example.core.domain.usecase.settings.GetAppSettingsUseCase
import com.example.core.domain.usecase.settings.SettingsUseCases
import com.example.core.domain.usecase.settings.UpdateAppSettingsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private lateinit var settingsUseCases: SettingsUseCases
    private lateinit var getAppSettingsUseCase: GetAppSettingsUseCase
    private lateinit var updateAppSettingsUseCase: UpdateAppSettingsUseCase
    private lateinit var viewModel: SettingsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAppSettingsUseCase = mockk()
        updateAppSettingsUseCase = mockk()
        settingsUseCases = mockk {
            every { getAppSettings } returns getAppSettingsUseCase
            every { updateAppSettings } returns updateAppSettingsUseCase
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should load settings from use case`() = runTest {
        // Given
        val testSettings = AppSettings(
            isDarkMode = true,
            fontSize = FontSize.LARGE
        )
        coEvery { getAppSettingsUseCase.invoke() } returns flow { emit(testSettings) }
        
        // When
        viewModel = SettingsViewModel(settingsUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertEquals(true, viewModel.isDarkMode.value)
        assertEquals(FontSize.LARGE, viewModel.fontSize.value)
    }

    @Test
    fun `toggleDarkMode should update dark mode state and save settings`() = runTest {
        // Given
        val initialSettings = AppSettings(isDarkMode = false, fontSize = FontSize.MEDIUM)
        coEvery { getAppSettingsUseCase.invoke() } returns flow { emit(initialSettings) }
        coEvery { updateAppSettingsUseCase.invoke(any()) } returns Unit
        
        viewModel = SettingsViewModel(settingsUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // When
        viewModel.toggleDarkMode()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertEquals(true, viewModel.isDarkMode.value)
        coVerify { updateAppSettingsUseCase.invoke(match { it.isDarkMode == true }) }
    }

    @Test
    fun `setFontSize should update font size state and save settings`() = runTest {
        // Given
        val initialSettings = AppSettings(isDarkMode = false, fontSize = FontSize.MEDIUM)
        coEvery { getAppSettingsUseCase.invoke() } returns flow { emit(initialSettings) }
        coEvery { updateAppSettingsUseCase.invoke(any()) } returns Unit
        
        viewModel = SettingsViewModel(settingsUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // When
        viewModel.setFontSize(FontSize.LARGE)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertEquals(FontSize.LARGE, viewModel.fontSize.value)
        coVerify { updateAppSettingsUseCase.invoke(match { it.fontSize == FontSize.LARGE }) }
    }

    @Test
    fun `should handle error when loading settings fails`() = runTest {
        // Given
        coEvery { getAppSettingsUseCase.invoke() } returns flow { throw Exception("Settings error") }
        
        // When
        viewModel = SettingsViewModel(settingsUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        // Should maintain default values
        assertEquals(false, viewModel.isDarkMode.value)
        assertEquals(FontSize.MEDIUM, viewModel.fontSize.value)
    }

    @Test
    fun `should handle error when saving settings fails`() = runTest {
        // Given
        val initialSettings = AppSettings(isDarkMode = false, fontSize = FontSize.MEDIUM)
        coEvery { getAppSettingsUseCase.invoke() } returns flow { emit(initialSettings) }
        coEvery { updateAppSettingsUseCase.invoke(any()) } throws Exception("Save error")
        
        viewModel = SettingsViewModel(settingsUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // When
        viewModel.toggleDarkMode()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        // UI state should rollback to original value when save fails
        assertEquals(false, viewModel.isDarkMode.value)  // Rollback ke false
        assertEquals("Save error", viewModel.error.value)  // Error message should be set
    }
}