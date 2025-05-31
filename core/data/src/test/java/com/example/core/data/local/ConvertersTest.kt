package com.example.core.data.local

import com.example.core.domain.model.RequestStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun `fromRequestStatus converts enum to string`() {
        assertEquals("PENDING", converters.fromRequestStatus(RequestStatus.PENDING))
        assertEquals("APPROVED", converters.fromRequestStatus(RequestStatus.APPROVED))
        assertEquals("REJECTED", converters.fromRequestStatus(RequestStatus.REJECTED))
    }

    @Test
    fun `toRequestStatus converts string to enum`() {
        assertEquals(RequestStatus.PENDING, converters.toRequestStatus("PENDING"))
        assertEquals(RequestStatus.APPROVED, converters.toRequestStatus("APPROVED"))
        assertEquals(RequestStatus.REJECTED, converters.toRequestStatus("REJECTED"))
    }
}
