package com.example.core.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.core.domain.model.AppSettings
import com.example.core.domain.model.FontSize
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var repository: SettingsRepositoryImpl

    @Before
    fun setup() {
        context = mockk()
        sharedPreferences = mockk()
        editor = mockk()

        every { context.getSharedPreferences(any(), any()) } returns sharedPreferences
        every { sharedPreferences.edit() } returns editor

        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.apply() } just Runs

        repository = SettingsRepositoryImpl(context)
    }

    @Test
    fun `getSettings should return Flow of current settings`() = runBlocking {
        // Given
        every { sharedPreferences.getBoolean("is_dark_mode", false) } returns true
        every { sharedPreferences.getString("font_size", FontSize.MEDIUM.name) } returns FontSize.LARGE.name

        // When
        val settings = repository.getSettings().first()

        // Then
        assertEquals(AppSettings(isDarkMode = true, fontSize = FontSize.LARGE), settings)
    }

    @Test
    fun `updateSettings should update shared preferences`() = runBlocking {
        // Given
        val newSettings = AppSettings(isDarkMode = false, fontSize = FontSize.SMALL)

        // When
        repository.updateSettings(newSettings)

        // Then
        verify(exactly = 1) { editor.putBoolean("is_dark_mode", newSettings.isDarkMode) }
        verify(exactly = 1) { editor.putString("font_size", newSettings.fontSize.name) }
        verify(exactly = 1) { editor.apply() }
    }
}


