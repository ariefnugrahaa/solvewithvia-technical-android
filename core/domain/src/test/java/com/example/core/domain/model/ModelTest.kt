package com.example.core.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ModelTest {

    @Test
    fun `Request data class properties are correct`() {
        val request = Request(
            id = "req1",
            title = "Test Request",
            description = "This is a test",
            status = RequestStatus.PENDING,
            createdAt = 123456789L
        )

        assertEquals("req1", request.id)
        assertEquals("Test Request", request.title)
        assertEquals("This is a test", request.description)
        assertEquals(RequestStatus.PENDING, request.status)
        assertEquals(123456789L, request.createdAt)
    }

    @Test
    fun `Request data class with default createdAt is correct`() {
         val request = Request(
             id = "req_default",
             title = "Default Request",
             description = "Default Description",
             status = RequestStatus.APPROVED
         )

        assertEquals("req_default", request.id)
        assertEquals("Default Request", request.title)
        assertEquals("Default Description", request.description)
        assertEquals(RequestStatus.APPROVED, request.status)
        assert(request.createdAt > 0)
    }

    @Test
    fun `Request data class equality and hashcode work correctly`() {
        val request1 = Request(
            id = "req1",
            title = "Test Request",
            description = "This is a test",
            status = RequestStatus.PENDING,
            createdAt = 123456789L
        )
        val request2 = Request(
            id = "req1",
            title = "Test Request",
            description = "This is a test",
            status = RequestStatus.PENDING,
            createdAt = 123456789L
        )
        val request3 = Request(
            id = "req2",
            title = "Another Request",
            description = "Another test",
            status = RequestStatus.APPROVED,
            createdAt = 987654321L
        )

        assertEquals(request1, request2)
        assertEquals(request1.hashCode(), request2.hashCode())
        assertNotEquals(request1, request3)
        assertNotEquals(request1.hashCode(), request3.hashCode())
    }

    @Test
    fun `Request data class copy works correctly`() {
        val originalRequest = Request(
            id = "req1",
            title = "Test Request",
            description = "This is a test",
            status = RequestStatus.PENDING,
            createdAt = 123456789L
        )

        val copiedRequest = originalRequest.copy(status = RequestStatus.APPROVED)

        assertEquals("req1", copiedRequest.id)
        assertEquals("Test Request", copiedRequest.title)
        assertEquals("This is a test", copiedRequest.description)
        assertEquals(RequestStatus.APPROVED, copiedRequest.status)
        assertEquals(123456789L, copiedRequest.createdAt)
    }

    @Test
    fun `AppSettings data class properties are correct`() {
        val appSettings = AppSettings(
            isDarkMode = true,
            fontSize = FontSize.LARGE
        )

        assertEquals(true, appSettings.isDarkMode)
        assertEquals(FontSize.LARGE, appSettings.fontSize)
    }

    @Test
    fun `AppSettings data class with default values is correct`() {
         val appSettings = AppSettings()

         assertEquals(false, appSettings.isDarkMode)
         assertEquals(FontSize.MEDIUM, appSettings.fontSize)
     }

    @Test
    fun `AppSettings data class equality and hashcode work correctly`() {
        val settings1 = AppSettings(isDarkMode = true, fontSize = FontSize.LARGE)
        val settings2 = AppSettings(isDarkMode = true, fontSize = FontSize.LARGE)
        val settings3 = AppSettings(isDarkMode = false, fontSize = FontSize.MEDIUM)

        assertEquals(settings1, settings2)
        assertEquals(settings1.hashCode(), settings2.hashCode())
        assertNotEquals(settings1, settings3)
        assertNotEquals(settings1.hashCode(), settings3.hashCode())
    }

    @Test
    fun `AppSettings data class copy works correctly`() {
        val originalSettings = AppSettings(isDarkMode = true, fontSize = FontSize.LARGE)

        val copiedSettings = originalSettings.copy(isDarkMode = false)

        assertEquals(false, copiedSettings.isDarkMode)
        assertEquals(FontSize.LARGE, copiedSettings.fontSize)
    }

    @Test
    fun `FontSize enum values are accessible`() {
        assertEquals(3, FontSize.entries.size)
        assertEquals(FontSize.SMALL, FontSize.valueOf("SMALL"))
        assertEquals(FontSize.MEDIUM, FontSize.valueOf("MEDIUM"))
        assertEquals(FontSize.LARGE, FontSize.valueOf("LARGE"))
    }
} 