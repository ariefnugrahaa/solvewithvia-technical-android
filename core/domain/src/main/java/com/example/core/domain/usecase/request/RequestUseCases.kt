package com.example.core.domain.usecase.request

import javax.inject.Inject

data class RequestUseCases @Inject constructor(
    val getAllRequests: GetAllRequestsUseCase,
    val getRequestById: GetRequestByIdUseCase,
    val createRequest: CreateRequestUseCase,
    val approveRequest: ApproveRequestUseCase,
    val rejectRequest: RejectRequestUseCase
) 