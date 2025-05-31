package com.example.core.domain.usecase.request

import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus
import com.example.core.domain.repository.RequestRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRequestByIdUseCaseTest {

    private lateinit var repository: RequestRepository
    private lateinit var useCase: GetRequestByIdUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetRequestByIdUseCase(repository)
    }

    @Test
    fun `when invoke is called, should return request from repository by id`() = runBlocking {
        // Given
        val testId = "test-request-id"
        val expectedRequest = Request(
            id = testId,
            title = "Test Title",
            description = "Test Description",
            status = RequestStatus.PENDING
        )
        coEvery { repository.getRequestById(testId) } returns expectedRequest

        // When
        val result = useCase(testId)

        // Then
        assertEquals(expectedRequest, result)
        coVerify(exactly = 1) { repository.getRequestById(testId) }
    }

    @Test
    fun `when invoke is called and request not found, should return null`() = runBlocking {
        // Given
        val testId = "non-existent-id"
        coEvery { repository.getRequestById(testId) } returns null

        // When
        val result = useCase(testId)

        // Then
        assertEquals(null, result)
        coVerify(exactly = 1) { repository.getRequestById(testId) }
    }
} 