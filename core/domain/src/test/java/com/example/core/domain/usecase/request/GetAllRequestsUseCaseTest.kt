package com.example.core.domain.usecase.request

import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus
import com.example.core.domain.repository.RequestRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAllRequestsUseCaseTest {

    private lateinit var repository: RequestRepository
    private lateinit var useCase: GetAllRequestsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetAllRequestsUseCase(repository)
    }

    @Test
    fun `when invoke is called, should return list of requests from repository`() = runBlocking {
        // Given
        val expectedRequests = listOf(
            Request(
                id = "1",
                title = "Test Request 1",
                description = "Description 1",
                status = RequestStatus.PENDING
            ),
            Request(
                id = "2",
                title = "Test Request 2",
                description = "Description 2",
                status = RequestStatus.APPROVED
            )
        )
        coEvery { repository.getAllRequests() } returns flowOf(expectedRequests)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expectedRequests, result)
    }
} 