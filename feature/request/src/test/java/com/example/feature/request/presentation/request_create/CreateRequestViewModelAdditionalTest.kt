package com.example.feature.request.presentation.request_create

import com.example.core.domain.model.RequestStatus
import com.example.core.domain.usecase.request.CreateRequestUseCase
import com.example.core.domain.usecase.request.RequestUseCases
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateRequestViewModelAdditionalTest {

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
    fun `should handle very long title`() = runTest {
        val longTitle = "A".repeat(1000)
        coEvery { createRequestUseCase.invoke(any()) } returns Unit
        
        viewModel.createRequest(longTitle, RequestStatus.PENDING)
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify { createRequestUseCase.invoke(match { it.title == longTitle }) }
    }

    @Test
    fun `should handle special characters in title`() = runTest {
        val specialTitle = "Test @#$%^&*()_+ Title"
        coEvery { createRequestUseCase.invoke(any()) } returns Unit
        
        viewModel.createRequest(specialTitle, RequestStatus.PENDING)
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify { createRequestUseCase.invoke(match { it.title == specialTitle }) }
    }

    @Test
    fun `should handle timeout exception`() = runTest {
        coEvery { createRequestUseCase.invoke(any()) } throws Exception("Request timeout")
        
        viewModel.createRequest("Test", RequestStatus.PENDING)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertTrue(state.error?.contains("timeout") == true)
    }

    @Test
    fun `should generate unique IDs for multiple requests`() = runTest {
        val capturedRequests = mutableListOf<String>()
        coEvery { createRequestUseCase.invoke(any()) } answers {
            capturedRequests.add(firstArg<com.example.core.domain.model.Request>().id)
        }
        
        viewModel.createRequest("Test 1", RequestStatus.PENDING)
        testDispatcher.scheduler.advanceUntilIdle()
        
        Thread.sleep(1)
        
        viewModel.createRequest("Test 2", RequestStatus.PENDING)
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(2, capturedRequests.size)
        assertTrue(capturedRequests[0] != capturedRequests[1])
    }

    @Test
    fun `should handle null error message`() = runTest {
        coEvery { createRequestUseCase.invoke(any()) } throws Exception()
        
        viewModel.createRequest("Test", RequestStatus.PENDING)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.state.value
        assertEquals("An error occurred while creating request", state.error)
    }
}