package com.example.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus

@Entity(tableName = "requests")
data class RequestEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val status: RequestStatus,
    val createdAt: Long
) {
    fun toRequest(): Request {
        return Request(
            id = id,
            title = title,
            description = description,
            status = status,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromRequest(request: Request): RequestEntity {
            return RequestEntity(
                id = request.id,
                title = request.title,
                description = request.description,
                status = request.status,
                createdAt = request.createdAt
            )
        }
    }
}
