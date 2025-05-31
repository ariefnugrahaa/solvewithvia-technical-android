package com.example.core.domain.model

data class Request(
    val id: String,
    val title: String,
    val description: String,
    val status: RequestStatus,
    val createdAt: Long = System.currentTimeMillis()
)