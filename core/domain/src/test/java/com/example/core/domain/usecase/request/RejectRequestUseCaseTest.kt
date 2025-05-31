package com.example.core.domain.usecase.request

import com.example.core.domain.repository.RequestRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class RejectRequestUseCaseTest {

    private lateinit var repository: RequestRepository
    private lateinit var useCase: RejectRequestUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = RejectRequestUseCase(repository)
    }

    @Test
    fun `when invoke is called, should call rejectRequest on repository with correct id`() = runBlocking {
        // Given
        val testId = "test-request-id"
        coEvery { repository.rejectRequest(any()) } just Runs

        // When
        useCase(testId)

        // Then
        coVerify(exactly = 1) { repository.rejectRequest(testId) }
    }
} 