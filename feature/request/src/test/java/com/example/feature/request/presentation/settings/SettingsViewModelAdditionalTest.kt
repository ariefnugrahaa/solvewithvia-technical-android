package com.example.feature.request.presentation.settings

import com.example.core.domain.model.AppSettings
import com.example.core.domain.model.FontSize
import com.example.core.domain.usecase.settings.GetAppSettingsUseCase
import com.example.core.domain.usecase.settings.SettingsUseCases
import com.example.core.domain.usecase.settings.UpdateAppSettingsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
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
class SettingsViewModelAdditionalTest {

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
            coEvery { getAppSettings } returns getAppSettingsUseCase
            coEvery { updateAppSettings } returns updateAppSettingsUseCase
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should handle all font sizes`() = runTest {
        val initialSettings = AppSettings(isDarkMode = false, fontSize = FontSize.MEDIUM)
        coEvery { getAppSettingsUseCase.invoke() } returns flow { emit(initialSettings) }
        coEvery { updateAppSettingsUseCase.invoke(any()) } returns Unit
        
        viewModel = SettingsViewModel(settingsUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        FontSize.entries.forEach { fontSize ->
            viewModel.setFontSize(fontSize)
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(fontSize, viewModel.fontSize.value)
        }
    }

    @Test
    fun `should handle rapid toggle operations`() = runTest {
        val initialSettings = AppSettings(isDarkMode = false, fontSize = FontSize.MEDIUM)
        coEvery { getAppSettingsUseCase.invoke() } returns flow { emit(initialSettings) }
        coEvery { updateAppSettingsUseCase.invoke(any()) } returns Unit
        
        viewModel = SettingsViewModel(settingsUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Rapid toggles
        repeat(5) {
            viewModel.toggleDarkMode()
            testDispatcher.scheduler.advanceUntilIdle()
        }
        
        // Should end up with dark mode enabled (odd number of toggles)
        assertEquals(true, viewModel.isDarkMode.value)
    }

    @Test
    fun `should handle concurrent font size changes`() = runTest {
        val initialSettings = AppSettings(isDarkMode = false, fontSize = FontSize.MEDIUM)
        coEvery { getAppSettingsUseCase.invoke() } returns flow { emit(initialSettings) }
        coEvery { updateAppSettingsUseCase.invoke(any()) } returns Unit
        
        viewModel = SettingsViewModel(settingsUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Rapid font size changes
        viewModel.setFontSize(FontSize.SMALL)
        viewModel.setFontSize(FontSize.LARGE)
        viewModel.setFontSize(FontSize.MEDIUM)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(FontSize.MEDIUM, viewModel.fontSize.value)
    }

    @Test
    fun `should verify correct settings are passed to update use case`() = runTest {
        val initialSettings = AppSettings(isDarkMode = false, fontSize = FontSize.MEDIUM)
        coEvery { getAppSettingsUseCase.invoke() } returns flow { emit(initialSettings) }
        coEvery { updateAppSettingsUseCase.invoke(any()) } returns Unit
        
        viewModel = SettingsViewModel(settingsUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.toggleDarkMode()
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify { 
            updateAppSettingsUseCase.invoke(
                match { settings ->
                    settings.isDarkMode == true && settings.fontSize == FontSize.MEDIUM
                }
            )
        }
    }
}