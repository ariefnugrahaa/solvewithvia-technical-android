package com.example.feature.request.presentation.request_create

import com.example.core.domain.model.RequestStatus
import com.example.core.domain.usecase.request.CreateRequestUseCase
import com.example.core.domain.usecase.request.RequestUseCases
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateRequestViewModelTest {

    private lateinit var requestUseCases: RequestUseCases
    private lateinit var createRequestUseCase: CreateRequestUseCase
    private lateinit var viewModel: CreateRequestViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        createRequestUseCase = mockk()
        requestUseCases = mockk {
            coEvery { createRequest } returns createRequestUseCase
        }
        viewModel = CreateRequestViewModel(requestUseCases)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should have default values`() = runTest {
        val initialState = viewModel.state.value
        assertEquals(false, initialState.isLoading)
        assertNull(initialState.error)
    }

    @Test
    fun `createRequest should create request and emit success event`() = runTest {
        val title = "Test Request"
        val status = RequestStatus.PENDING

        coEvery { createRequestUseCase.invoke(any()) } returns Unit

        viewModel.createRequest(title, status)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { createRequestUseCase.invoke(any()) }
        
        val event = viewModel.events.first()
        assertTrue(event is CreateRequestViewModel.UiEvent.Success)
        assertEquals(status, (event as CreateRequestViewModel.UiEvent.Success).status)
    }

    @Test
    fun `createRequest should create request with correct data`() = runTest {
        val title = "Test Request"
        val status = RequestStatus.PENDING

        coEvery { createRequestUseCase.invoke(match { request ->
            request.title == title &&
            request.status == status &&
            request.description == "" &&
            request.id.isNotEmpty()
        }) } returns Unit

        viewModel.createRequest(title, status)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { createRequestUseCase.invoke(match { request ->
            request.title == title &&
            request.status == status &&
            request.description == "" &&
            request.id.isNotEmpty()
        }) }
    }
    
    @Test
    fun `createRequest should handle error and update state`() = runTest {
        val title = "Test Request"
        val status = RequestStatus.PENDING
        val errorMessage = "Error creating request"

        coEvery { createRequestUseCase.invoke(any()) } throws Exception(errorMessage)

        viewModel.createRequest(title, status)
        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.state.value
        assertTrue(currentState.error?.contains(errorMessage) == true)
        assertEquals(false, currentState.isLoading)
    }

    @Test
    fun `createRequest should update loading state`() = runTest {
        val title = "Test Request"
        val status = RequestStatus.PENDING

        coEvery { createRequestUseCase.invoke(any()) } returns Unit

        viewModel.createRequest(title, status)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val currentState = viewModel.state.value
        assertEquals(false, currentState.isLoading)
    }

    @Test
    fun `createRequest should validate input data`() = runTest {
        val title = ""
        val status = RequestStatus.PENDING

        viewModel.createRequest(title, status)
        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.state.value
        assertTrue(currentState.error?.contains("Title") == true)
        assertEquals(false, currentState.isLoading)
    }

    @Test
    fun `createRequest should handle different request statuses`() = runTest {
        val title = "Test Request"
        val statuses = listOf(RequestStatus.PENDING, RequestStatus.APPROVED, RequestStatus.REJECTED)
        
        statuses.forEach { status ->
            coEvery { createRequestUseCase.invoke(any()) } returns Unit
            
            viewModel.createRequest(title, status)
            testDispatcher.scheduler.advanceUntilIdle()
            
            val event = viewModel.events.first()
            assertTrue(event is CreateRequestViewModel.UiEvent.Success)
            assertEquals(status, (event as CreateRequestViewModel.UiEvent.Success).status)
        }
    }

    @Test
    fun `createRequest should validate title with whitespace only`() = runTest {
        val title = "   "
        val status = RequestStatus.PENDING

        viewModel.createRequest(title, status)
        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.state.value
        assertTrue(currentState.error?.contains("Title") == true)
        assertEquals(false, currentState.isLoading)
    }

    @Test
    fun `createRequest should reset error state on successful creation`() = runTest {
        // First create an error state
        viewModel.createRequest("", RequestStatus.PENDING)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Verify error exists
        assertTrue(viewModel.state.value.error != null)
        
        // Then create a successful request
        coEvery { createRequestUseCase.invoke(any()) } returns Unit
        viewModel.createRequest("Valid Title", RequestStatus.PENDING)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Verify error is cleared
        assertEquals(null, viewModel.state.value.error)
    }

    @Test
    fun `initial state should have correct default values`() {
        val initialState = viewModel.state.value
        assertEquals(false, initialState.isLoading)
        assertEquals(null, initialState.error)
    }
}