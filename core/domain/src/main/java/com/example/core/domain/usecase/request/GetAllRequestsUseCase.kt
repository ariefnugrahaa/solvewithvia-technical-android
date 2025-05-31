package com.example.core.domain.usecase.request

import com.example.core.domain.model.Request
import com.example.core.domain.repository.RequestRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRequestsUseCase @Inject constructor(
    private val repository: RequestRepository
) {
    operator fun invoke(): Flow<List<Request>> = repository.getAllRequests()
} 