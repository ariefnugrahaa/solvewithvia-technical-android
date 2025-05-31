package com.example.feature.request.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.AppSettings
import com.example.core.domain.model.FontSize
import com.example.core.domain.usecase.settings.SettingsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _fontSize = MutableStateFlow(FontSize.MEDIUM)
    val fontSize: StateFlow<FontSize> = _fontSize.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                settingsUseCases.getAppSettings()
                    .catch { e ->
                        _error.value = e.message ?: "Failed to load settings"
                    }
                    .collectLatest { settings ->
                        _isDarkMode.value = settings.isDarkMode
                        _fontSize.value = settings.fontSize
                        _error.value = null
                    }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load settings"
            }
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            val newDarkMode = !_isDarkMode.value
            _isDarkMode.value = newDarkMode
            val currentSettings = AppSettings(
                isDarkMode = newDarkMode,
                fontSize = _fontSize.value
            )
            try {
                settingsUseCases.updateAppSettings(currentSettings)
                _error.value = null
            } catch (e: Exception) {
                _isDarkMode.value = !newDarkMode
                _error.value = e.message ?: "Failed to save dark mode setting"
            }
        }
    }

    fun setFontSize(fontSize: FontSize) {
        viewModelScope.launch {
            val previousFontSize = _fontSize.value
            _fontSize.value = fontSize
            val currentSettings = AppSettings(
                isDarkMode = _isDarkMode.value,
                fontSize = fontSize
            )
            try {
                settingsUseCases.updateAppSettings(currentSettings)
                _error.value = null
            } catch (e: Exception) {
                _fontSize.value = previousFontSize
                _error.value = e.message ?: "Failed to save font size setting"
            }
        }
    }
}