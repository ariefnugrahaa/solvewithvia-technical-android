package com.example.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core.data.local.entity.RequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {
    @Query("SELECT * FROM requests ORDER BY createdAt DESC")
    fun getAllRequests(): Flow<List<RequestEntity>>

    @Query("SELECT * FROM requests WHERE id = :id")
    suspend fun getRequestById(id: String): RequestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: RequestEntity)

    @Update
    suspend fun updateRequest(request: RequestEntity)

    @Delete
    suspend fun deleteRequest(request: RequestEntity)

    @Query("UPDATE requests SET status = 'APPROVED' WHERE id = :id")
    suspend fun approveRequest(id: String)

    @Query("UPDATE requests SET status = 'REJECTED' WHERE id = :id")
    suspend fun rejectRequest(id: String)
}

