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

class ApproveRequestUseCaseTest {

    private lateinit var repository: RequestRepository
    private lateinit var useCase: ApproveRequestUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = ApproveRequestUseCase(repository)
    }

    @Test
    fun `when invoke is called, should call approveRequest on repository with correct id`() = runBlocking {
        // Given
        val testId = "test-request-id"
        coEvery { repository.approveRequest(any()) } just Runs

        // When
        useCase(testId)

        // Then
        coVerify(exactly = 1) { repository.approveRequest(testId) }
    }
} 