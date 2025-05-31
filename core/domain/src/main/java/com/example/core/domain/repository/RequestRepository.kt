package com.example.core.domain.repository

import com.example.core.domain.model.Request
import kotlinx.coroutines.flow.Flow

interface RequestRepository {
    fun getAllRequests(): Flow<List<Request>>
    suspend fun getRequestById(id: String): Request?
    suspend fun createRequest(request: Request)
    suspend fun updateRequest(request: Request)
    suspend fun deleteRequest(id: String)
    suspend fun approveRequest(id: String)
    suspend fun rejectRequest(id: String)
} 