package com.example.feature.request.presentation.request_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus
import com.example.core.domain.usecase.request.RequestUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRequestViewModel @Inject constructor(
    private val requestUseCases: RequestUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(CreateRequestState())
    val state: StateFlow<CreateRequestState> = _state

    private val _eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val events = _eventChannel.receiveAsFlow()

    fun createRequest(title: String, status: RequestStatus) {
        viewModelScope.launch {
            try {
                if (title.isBlank()) {
                    _state.value = _state.value.copy(
                        error = "Title cannot be empty",
                        isLoading = false
                    )
                    return@launch
                }
                
                _state.value = _state.value.copy(isLoading = true, error = null)
                
                val request = Request(
                    id = System.currentTimeMillis().toString(),
                    title = title,
                    description = "",
                    status = status,
                    createdAt = System.currentTimeMillis()
                )
                
                requestUseCases.createRequest(request)
                
                _state.value = _state.value.copy(isLoading = false, error = null)
                _eventChannel.send(UiEvent.Success(status))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred while creating request"
                )
            }
        }
    }

    sealed class UiEvent {
        data class Success(val status: RequestStatus) : UiEvent()
    }
}