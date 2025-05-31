package com.example.feature.request.presentation.request_list

import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus
import com.example.core.domain.usecase.request.RequestUseCases
import com.example.core.domain.usecase.request.GetAllRequestsUseCase
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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RequestListViewModelTest {

    private lateinit var requestUseCases: RequestUseCases
    private lateinit var getAllRequestsUseCase: GetAllRequestsUseCase
    private lateinit var viewModel: RequestListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getAllRequestsUseCase = mockk()
        requestUseCases = mockk {
            every { getAllRequests } returns getAllRequestsUseCase
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have default values`() = runTest {
        // Given
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(emptyList()) }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        
        // Then
        val state = viewModel.state.value
        assertEquals(emptyList<Request>(), state.requests)
        assertEquals(true, state.isLoading)
        assertNull(state.error)
        assertEquals(RequestFilterStatus.ALL, state.filterStatus)
        assertEquals(RequestSortOrder.NEWEST_FIRST, state.sortOrder)
    }

    @Test
    fun `when loading completes, isLoading should be false`() = runTest {
        // Given
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(emptyList()) }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.state.value
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `when requests are loaded, state should contain those requests`() = runTest {
        // Given
        val testRequests = listOf(
            Request(
                id = "1",
                title = "Test Request 1",
                description = "Description 1",
                status = RequestStatus.APPROVED,
                createdAt = System.currentTimeMillis()
            )
        )
        
        coEvery { getAllRequestsUseCase.invoke() } returns flow { 
            emit(testRequests)
        }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.state.value
        assertEquals(1, state.requests.size)
        assertEquals("1", state.requests[0].id)
        assertEquals(RequestStatus.APPROVED, state.requests[0].status)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `when error occurs, state should contain error message`() = runTest {
        // Given
        val errorMessage = "Test error"
        coEvery { getAllRequestsUseCase.invoke() } returns flow { throw Exception(errorMessage) }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.state.value
        assertEquals(errorMessage, state.error)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `when filter is set to APPROVED, only approved requests should be shown`() = runTest {
        // Given
        val testRequests = listOf(
            Request(
                id = "1",
                title = "Test Request 1",
                description = "Description 1",
                status = RequestStatus.APPROVED,
                createdAt = System.currentTimeMillis()
            ),
            Request(
                id = "2",
                title = "Test Request 2",
                description = "Description 2",
                status = RequestStatus.REJECTED,
                createdAt = System.currentTimeMillis()
            )
        )
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(testRequests) }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.setFilterStatus(RequestFilterStatus.APPROVED)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.state.value
        assertEquals(1, state.requests.size)
        assertEquals(RequestStatus.APPROVED, state.requests[0].status)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `when sort order is set to OLDEST_FIRST, requests should be sorted accordingly`() = runTest {
        // Given
        val oldTime = System.currentTimeMillis() - 1000
        val newTime = System.currentTimeMillis()
        val testRequests = listOf(
            Request(
                id = "1",
                title = "Test Request 1",
                description = "Description 1",
                status = RequestStatus.APPROVED,
                createdAt = oldTime
            ),
            Request(
                id = "2",
                title = "Test Request 2",
                description = "Description 2",
                status = RequestStatus.APPROVED,
                createdAt = newTime
            )
        )
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(testRequests) }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.setSortOrder(RequestSortOrder.OLDEST_FIRST)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.state.value
        assertEquals(2, state.requests.size)
        assertEquals("1", state.requests[0].id) // Oldest first
        assertEquals("2", state.requests[1].id)
        assertEquals(false, state.isLoading)
    }

    @Test
    fun `when filter is set to REJECTED, only rejected requests should be shown`() = runTest {
        // Given
        val testRequests = listOf(
            Request(
                id = "1",
                title = "Approved Request",
                description = "Description 1",
                status = RequestStatus.APPROVED,
                createdAt = System.currentTimeMillis()
            ),
            Request(
                id = "2",
                title = "Rejected Request",
                description = "Description 2",
                status = RequestStatus.REJECTED,
                createdAt = System.currentTimeMillis()
            )
        )
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(testRequests) }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.setFilterStatus(RequestFilterStatus.REJECTED)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.state.value
        assertEquals(1, state.requests.size)
        assertEquals(RequestStatus.REJECTED, state.requests[0].status)
        assertEquals("2", state.requests[0].id)
    }

    @Test
    fun `when multiple filters and sorts are applied, should work correctly`() = runTest {
        // Given
        val oldTime = System.currentTimeMillis() - 2000
        val newTime = System.currentTimeMillis()
        val testRequests = listOf(
            Request(
                id = "1",
                title = "Old Approved",
                description = "Description 1",
                status = RequestStatus.APPROVED,
                createdAt = oldTime
            ),
            Request(
                id = "2",
                title = "New Approved",
                description = "Description 2",
                status = RequestStatus.APPROVED,
                createdAt = newTime
            ),
            Request(
                id = "3",
                title = "Rejected",
                description = "Description 3",
                status = RequestStatus.REJECTED,
                createdAt = newTime
            )
        )
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(testRequests) }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.setFilterStatus(RequestFilterStatus.APPROVED)
        viewModel.setSortOrder(RequestSortOrder.OLDEST_FIRST)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.state.value
        assertEquals(2, state.requests.size)
        assertEquals("1", state.requests[0].id) // Oldest approved first
        assertEquals("2", state.requests[1].id) // Newest approved second
    }

    @Test
    fun `loadRequests should be called on initialization`() = runTest {
        // Given
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(emptyList()) }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        coVerify { getAllRequestsUseCase.invoke() }
    }

    @Test
    fun `state should have correct initial values`() {
        // Given
        coEvery { getAllRequestsUseCase.invoke() } returns flow { emit(emptyList()) }
        
        // When
        viewModel = RequestListViewModel(requestUseCases)
        
        // Then (before any async operations)
        val initialState = viewModel.state.value
        assertEquals(true, initialState.isLoading)
        assertEquals(RequestFilterStatus.ALL, initialState.filterStatus)
        assertEquals(RequestSortOrder.NEWEST_FIRST, initialState.sortOrder)
    }
}