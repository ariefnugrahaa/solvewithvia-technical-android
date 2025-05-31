package com.example.core.domain.usecase.request

import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test

class RequestUseCasesTest {

    @Test
    fun `RequestUseCases can be instantiated and properties accessed`() {
        // Given
        val getAllRequestsUseCase = mockk<GetAllRequestsUseCase>()
        val getRequestByIdUseCase = mockk<GetRequestByIdUseCase>()
        val createRequestUseCase = mockk<CreateRequestUseCase>()
        val approveRequestUseCase = mockk<ApproveRequestUseCase>()
        val rejectRequestUseCase = mockk<RejectRequestUseCase>()

        // When
        val requestUseCases = RequestUseCases(
            getAllRequests = getAllRequestsUseCase,
            getRequestById = getRequestByIdUseCase,
            createRequest = createRequestUseCase,
            approveRequest = approveRequestUseCase,
            rejectRequest = rejectRequestUseCase
        )

        // Then
        assertNotNull(requestUseCases.getAllRequests)
        assertNotNull(requestUseCases.getRequestById)
        assertNotNull(requestUseCases.createRequest)
        assertNotNull(requestUseCases.approveRequest)
        assertNotNull(requestUseCases.rejectRequest)
    }
} 