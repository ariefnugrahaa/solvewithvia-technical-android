package com.example.core.data.repository

import com.example.core.data.local.RequestDatabase
import com.example.core.data.local.dao.RequestDao
import com.example.core.data.local.entity.RequestEntity
import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RequestRepositoryImplTest {

    private lateinit var database: RequestDatabase
    private lateinit var dao: RequestDao
    private lateinit var repository: RequestRepositoryImpl

    @Before
    fun setup() {
        database = mockk()
        dao = mockk()
        every { database.dao } returns dao

        repository = RequestRepositoryImpl(database)
    }

    @Test
    fun `getAllRequests should return Flow of domain requests`() = runBlocking {
        // Given
        val entityList = listOf(
            RequestEntity("1", "Title 1", "Desc 1", RequestStatus.PENDING, 100L),
            RequestEntity("2", "Title 2", "Desc 2", RequestStatus.APPROVED, 200L)
        )
        val domainList = entityList.map { it.toRequest() }

        every { dao.getAllRequests() } returns flowOf(entityList)

        // When
        val result = repository.getAllRequests().first()

        // Then
        assertEquals(domainList, result)
    }

    @Test
    fun `getRequestById should return domain request when found`() = runBlocking {
        // Given
        val entity = RequestEntity("1", "Title 1", "Desc 1", RequestStatus.PENDING, 100L)
        val domainRequest = entity.toRequest()
        coEvery { dao.getRequestById("1") } returns entity

        // When
        val result = repository.getRequestById("1")

        // Then
        assertEquals(domainRequest, result)
    }

    @Test
    fun `getRequestById should return null when not found`() = runBlocking {
        // Given
        coEvery { dao.getRequestById("non-existent") } returns null

        // When
        val result = repository.getRequestById("non-existent")

        // Then
        assertNull(result)
    }

    @Test
    fun `createRequest should insert request entity`() = runBlocking {
        // Given
        val domainRequest = Request("3", "New Title", "New Desc", RequestStatus.REJECTED, 300L)
        val entity = RequestEntity.fromRequest(domainRequest)
        coEvery { dao.insertRequest(entity) } returns Unit

        // When
        repository.createRequest(domainRequest)

        // Then
        coVerify(exactly = 1) { dao.insertRequest(entity) }
    }

    @Test
    fun `updateRequest should update request entity`() = runBlocking {
        // Given
        val domainRequest = Request("1", "Updated Title", "Updated Desc", RequestStatus.APPROVED, 100L)
        val entity = RequestEntity.fromRequest(domainRequest)
        coEvery { dao.updateRequest(entity) } returns Unit

        // When
        repository.updateRequest(domainRequest)

        // Then
        coVerify(exactly = 1) { dao.updateRequest(entity) }
    }

    @Test
    fun `deleteRequest should delete request entity when found`() = runBlocking {
        // Given
        val entity = RequestEntity("1", "Title 1", "Desc 1", RequestStatus.PENDING, 100L)
        coEvery { dao.getRequestById("1") } returns entity
        coEvery { dao.deleteRequest(entity) } returns Unit

        // When
        repository.deleteRequest("1")

        // Then
        coVerify(exactly = 1) { dao.getRequestById("1") }
        coVerify(exactly = 1) { dao.deleteRequest(entity) }
    }

    @Test
    fun `deleteRequest should do nothing when request not found`() = runBlocking {
        // Given
        coEvery { dao.getRequestById("non-existent") } returns null

        // When
        repository.deleteRequest("non-existent")

        // Then
        coVerify(exactly = 1) { dao.getRequestById("non-existent") }
        coVerify(exactly = 0) { dao.deleteRequest(any()) }
    }

    @Test
    fun `approveRequest should call dao approveRequest`() = runBlocking {
        // Given
        val id = "request-to-approve"
        coEvery { dao.approveRequest(id) } returns Unit

        // When
        repository.approveRequest(id)

        // Then
        coVerify(exactly = 1) { dao.approveRequest(id) }
    }

    @Test
    fun `rejectRequest should call dao rejectRequest`() = runBlocking {
        // Given
        val id = "request-to-reject"
        coEvery { dao.rejectRequest(id) } returns Unit

        // When
        repository.rejectRequest(id)

        // Then
        coVerify(exactly = 1) { dao.rejectRequest(id) }
    }
}

