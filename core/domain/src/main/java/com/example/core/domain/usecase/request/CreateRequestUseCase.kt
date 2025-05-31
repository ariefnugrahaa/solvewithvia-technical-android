package com.example.core.domain.usecase.request

import com.example.core.domain.model.Request
import com.example.core.domain.repository.RequestRepository
import javax.inject.Inject

class CreateRequestUseCase @Inject constructor(
    private val repository: RequestRepository
) {
    suspend operator fun invoke(request: Request) {
        try {
            repository.createRequest(request)
        } catch (e: Exception) {
            throw Exception("Failed to create request: ${e.message}", e)
        }
    }
}