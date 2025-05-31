package com.example.core.data.local

import androidx.room.TypeConverter
import com.example.core.domain.model.RequestStatus

class Converters {
    @TypeConverter
    fun fromRequestStatus(status: RequestStatus): String {
        return status.name
    }

    @TypeConverter
    fun toRequestStatus(status: String): RequestStatus {
        return RequestStatus.valueOf(status)
    }
}

