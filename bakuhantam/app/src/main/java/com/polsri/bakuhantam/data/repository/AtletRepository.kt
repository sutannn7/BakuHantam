package com.polsri.bakuhantam.data.repository

import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.database.entity.Atlet
import kotlinx.coroutines.flow.Flow

class AtletRepository(private val db: AppDatabase) {
    val atletList: Flow<List<Atlet>> = db.atletDao().getAll()

    suspend fun insert(atlet: Atlet) = db.atletDao().insert(atlet)
    suspend fun update(atlet: Atlet) = db.atletDao().update(atlet)
    suspend fun getById(id: Int) = db.atletDao().getById(id)
}
