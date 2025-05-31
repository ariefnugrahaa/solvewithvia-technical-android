package com.example.core.data.local.entity

import com.example.core.domain.model.Request
import com.example.core.domain.model.RequestStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class RequestEntityTest {

    @Test
    fun `RequestEntity properties are correct`() {
        val entity = RequestEntity(
            id = "ent1",
            title = "Entity Title",
            description = "Entity Desc",
            status = RequestStatus.APPROVED,
            createdAt = 987654321L
        )

        assertEquals("ent1", entity.id)
        assertEquals("Entity Title", entity.title)
        assertEquals("Entity Desc", entity.description)
        assertEquals(RequestStatus.APPROVED, entity.status)
        assertEquals(987654321L, entity.createdAt)
    }

    @Test
    fun `RequestEntity equality and hashcode work correctly`() {
        val entity1 = RequestEntity(
            id = "ent1",
            title = "Entity Title",
            description = "Entity Desc",
            status = RequestStatus.APPROVED,
            createdAt = 987654321L
        )
        val entity2 = RequestEntity(
            id = "ent1",
            title = "Entity Title",
            description = "Entity Desc",
            status = RequestStatus.APPROVED,
            createdAt = 987654321L
        )
        val entity3 = RequestEntity(
            id = "ent2",
            title = "Another Entity",
            description = "Another Desc",
            status = RequestStatus.PENDING,
            createdAt = 123456789L
        )

        assertEquals(entity1, entity2)
        assertEquals(entity1.hashCode(), entity2.hashCode())
        assertNotEquals(entity1, entity3)
        assertNotEquals(entity1.hashCode(), entity3.hashCode())
    }

    @Test
    fun `toRequest maps RequestEntity to domain Request correctly`() {
        val entity = RequestEntity(
            id = "ent1",
            title = "Entity Title",
            description = "Entity Desc",
            status = RequestStatus.REJECTED,
            createdAt = 987654321L
        )
        val expectedDomainRequest = Request(
            id = "ent1",
            title = "Entity Title",
            description = "Entity Desc",
            status = RequestStatus.REJECTED,
            createdAt = 987654321L
        )

        val domainRequest = entity.toRequest()

        assertEquals(expectedDomainRequest, domainRequest)
    }

    @Test
    fun `fromRequest maps domain Request to RequestEntity correctly`() {
        val domainRequest = Request(
            id = "req1",
            title = "Domain Title",
            description = "Domain Desc",
            status = RequestStatus.PENDING,
            createdAt = 123456789L
        )
        val expectedEntity = RequestEntity(
            id = "req1",
            title = "Domain Title",
            description = "Domain Desc",
            status = RequestStatus.PENDING,
            createdAt = 123456789L
        )

        val entity = RequestEntity.fromRequest(domainRequest)

        assertEquals(expectedEntity, entity)
    }
}

