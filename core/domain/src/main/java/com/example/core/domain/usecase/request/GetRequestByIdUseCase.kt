package com.example.core.domain.usecase.request

import com.example.core.domain.model.Request
import com.example.core.domain.repository.RequestRepository
import javax.inject.Inject

class GetRequestByIdUseCase @Inject constructor(
    private val repository: RequestRepository
) {
    suspend operator fun invoke(id: String): Request? = repository.getRequestById(id)
} 