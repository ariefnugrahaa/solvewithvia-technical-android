package com.example.feature.request.presentation.request_list

import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus
import com.example.core.domain.usecase.request.GetAllRequestsUseCase
import com.example.core.domain.usecase.request.RequestUseCases
import io.mockk.coEvery
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
class RequestListViewModelAdditionalTest {

    private lateinit var requestUseCases: RequestUseCases
    private lateinit var getAllRequestsUseCase: GetAllRequestsUseCase
    private lateinit var viewModel: RequestListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAllRequestsUseCase = mockk()
        requestUseCases = mockk {
            coEvery { getAllRequests } returns getAllRequestsUseCase
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should handle empty request list`() = runTest {
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(emptyList()) }
        
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals(emptyList<Request>(), state.requests)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `should handle network error during loading`() = runTest {
        coEvery { getAllRequestsUseCase.invoke() } returns flow { throw Exception("Network error") }
        
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals("Network error", state.error)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `should sort requests by oldest first`() = runTest {
        val requests = listOf(
            Request("1", "New", "", RequestStatus.PENDING, 2000L),
            Request("2", "Old", "", RequestStatus.PENDING, 1000L)
        )
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(requests) }
        
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.setSortOrder(RequestSortOrder.OLDEST_FIRST)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals("Old", state.requests.first().title)
    }

    @Test
    fun `should filter by rejected status`() = runTest {
        val requests = listOf(
            Request("1", "Approved", "", RequestStatus.APPROVED, 1000L),
            Request("2", "Rejected", "", RequestStatus.REJECTED, 1000L)
        )
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(requests) }
        
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.setFilterStatus(RequestFilterStatus.REJECTED)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals(1, state.requests.size)
        assertEquals("Rejected", state.requests.first().title)
    }

    @Test
    fun `should handle multiple filter and sort combinations`() = runTest {
        val requests = listOf(
            Request("1", "New Approved", "", RequestStatus.APPROVED, 2000L),
            Request("2", "Old Approved", "", RequestStatus.APPROVED, 1000L),
            Request("3", "Pending", "", RequestStatus.PENDING, 1500L)
        )
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(requests) }
        
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.setFilterStatus(RequestFilterStatus.APPROVED)
        viewModel.setSortOrder(RequestSortOrder.OLDEST_FIRST)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals(2, state.requests.size)
        assertEquals("Old Approved", state.requests.first().title)
    }
}