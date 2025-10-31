package com.example.mytaskflow.data

import kotlinx.coroutines.flow.Flow

class HubRepository(private val hubDao: HubDao) {

    fun getAllItems(): Flow<List<HubItem>> = hubDao.getAllItems()

    suspend fun insert(item: HubItem) {
        hubDao.insert(item)
    }

    suspend fun deleteById(id: Long) {
        hubDao.deleteById(id)
    }
}