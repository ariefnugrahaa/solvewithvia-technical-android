package com.example.core.domain.usecase.request

import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus
import com.example.core.domain.repository.RequestRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CreateRequestUseCaseTest {

    private lateinit var repository: RequestRepository
    private lateinit var useCase: CreateRequestUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = CreateRequestUseCase(repository)
    }

    @Test
    fun `when invoke is called, should call createRequest on repository with correct request`() = runBlocking {
        // Given
        val testRequest = Request(
            id = "test-id",
            title = "Test Title",
            description = "Test Description",
            status = RequestStatus.PENDING
        )

        coEvery { repository.createRequest(any()) } just Runs

        // When
        useCase(testRequest)

        // Then
        coVerify(exactly = 1) { repository.createRequest(testRequest) }
    }
} 