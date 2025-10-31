package com.example.mytaskflow.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * DAO для управления Подзадачами (SubTask)
 */
@Dao
interface SubTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subTask: SubTask)

    @Update
    suspend fun update(subTask: SubTask)

    @Delete
    suspend fun delete(subTask: SubTask)
}