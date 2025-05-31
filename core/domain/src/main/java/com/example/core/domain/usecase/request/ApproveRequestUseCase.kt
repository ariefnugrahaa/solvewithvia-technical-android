package com.example.core.domain.usecase.request

import com.example.core.domain.repository.RequestRepository
import javax.inject.Inject

class ApproveRequestUseCase @Inject constructor(
    private val repository: RequestRepository
) {
    suspend operator fun invoke(id: String) = repository.approveRequest(id)
} 