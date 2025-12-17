package com.polsri.bakuhantam.data.repository

import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.database.entity.Wasit
import kotlinx.coroutines.flow.Flow

class WasitRepository(private val db: AppDatabase) {
    val wasitList: Flow<List<Wasit>> = db.wasitDao().getAll()
    suspend fun insert(w: Wasit) = db.wasitDao().insert(w)
}
