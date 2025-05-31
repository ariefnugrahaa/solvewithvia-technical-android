package com.example.core.data.repository

import com.example.core.data.local.RequestDatabase
import com.example.core.data.local.entity.RequestEntity
import com.example.core.domain.model.Request
import com.example.core.domain.repository.RequestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RequestRepositoryImpl @Inject constructor(
    private val db: RequestDatabase
) : RequestRepository {

    override fun getAllRequests(): Flow<List<Request>> {
        return db.dao.getAllRequests()
            .map { entities ->
                entities.map { it.toRequest() }
            }
            .catch { e ->
                throw Exception("Failed to fetch requests: ${e.message}", e)
            }
    }

    override suspend fun getRequestById(id: String): Request? {
        return try {
            db.dao.getRequestById(id)?.toRequest()
        } catch (e: Exception) {
            throw Exception("Failed to fetch request with id $id: ${e.message}", e)
        }
    }

    override suspend fun createRequest(request: Request) {
        try {
            db.dao.insertRequest(RequestEntity.fromRequest(request))
        } catch (e: Exception) {
            throw Exception("Failed to create request: ${e.message}", e)
        }
    }

    override suspend fun updateRequest(request: Request) {
        try {
            db.dao.updateRequest(RequestEntity.fromRequest(request))
        } catch (e: Exception) {
            throw Exception("Failed to update request: ${e.message}", e)
        }
    }

    override suspend fun deleteRequest(id: String) {
        try {
            getRequestById(id)?.let { request ->
                db.dao.deleteRequest(RequestEntity.fromRequest(request))
            }
        } catch (e: Exception) {
            throw Exception("Failed to delete request with id $id: ${e.message}", e)
        }
    }

    override suspend fun approveRequest(id: String) {
        try {
            db.dao.approveRequest(id)
        } catch (e: Exception) {
            throw Exception("Failed to approve request with id $id: ${e.message}", e)
        }
    }

    override suspend fun rejectRequest(id: String) {
        try {
            db.dao.rejectRequest(id)
        } catch (e: Exception) {
            throw Exception("Failed to reject request with id $id: ${e.message}", e)
        }
    }
}

