package com.example.mytaskflow.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HubDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: HubItem)

    @Query("SELECT * FROM hub_items ORDER BY timestamp DESC")
    fun getAllItems(): Flow<List<HubItem>>

    @Query("DELETE FROM hub_items WHERE id = :id")
    suspend fun deleteById(id: Long)
}