package com.example.core.domain.usecase.request

import com.example.core.domain.repository.RequestRepository
import javax.inject.Inject

class RejectRequestUseCase @Inject constructor(
    private val repository: RequestRepository
) {
    suspend operator fun invoke(id: String) = repository.rejectRequest(id)
} 