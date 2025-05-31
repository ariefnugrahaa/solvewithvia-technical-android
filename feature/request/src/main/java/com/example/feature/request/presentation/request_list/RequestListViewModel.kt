package com.example.feature.request.presentation.request_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus
import com.example.core.domain.usecase.request.RequestUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestListViewModel @Inject constructor(
    private val requestUseCases: RequestUseCases
) : ViewModel() {

    private val _requests = MutableStateFlow<List<Request>>(emptyList())
    private val _filterStatus = MutableStateFlow(RequestFilterStatus.ALL)
    private val _sortOrder = MutableStateFlow(RequestSortOrder.NEWEST_FIRST)
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<RequestListState> = combine(
        _requests,
        _filterStatus,
        _sortOrder,
        _isLoading,
        _error
    ) { requests, filterStatus, sortOrder, isLoading, error ->
        val filteredRequests = when (filterStatus) {
            RequestFilterStatus.ALL -> requests
            RequestFilterStatus.APPROVED -> requests.filter { it.status == RequestStatus.APPROVED }
            RequestFilterStatus.REJECTED -> requests.filter { it.status == RequestStatus.REJECTED }
        }

        val sortedRequests = when (sortOrder) {
            RequestSortOrder.NEWEST_FIRST -> filteredRequests.sortedByDescending { it.createdAt }
            RequestSortOrder.OLDEST_FIRST -> filteredRequests.sortedBy { it.createdAt }
        }

        RequestListState(
            requests = sortedRequests,
            isLoading = isLoading,
            error = error,
            filterStatus = filterStatus,
            sortOrder = sortOrder
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = RequestListState()
    )

    init {
        loadRequests()
    }

    fun loadRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                requestUseCases.getAllRequests.invoke()
                    .catch { e ->
                        _error.value = e.message ?: "An error occurred"
                        _isLoading.value = false
                    }
                    .collect { requests ->
                        _requests.value = requests
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                _isLoading.value = false
            }
        }
    }

    fun setFilterStatus(status: RequestFilterStatus) {
        _filterStatus.value = status
    }

    fun setSortOrder(sortOrder: RequestSortOrder) {
        _sortOrder.value = sortOrder
    }
}

data class RequestListState(
    val requests: List<Request> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val filterStatus: RequestFilterStatus = RequestFilterStatus.ALL,
    val sortOrder: RequestSortOrder = RequestSortOrder.NEWEST_FIRST
)

enum class RequestFilterStatus {
    ALL,
    APPROVED,
    REJECTED
}

enum class RequestSortOrder {
    NEWEST_FIRST,
    OLDEST_FIRST
}