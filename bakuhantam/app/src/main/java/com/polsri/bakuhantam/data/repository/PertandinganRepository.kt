package com.polsri.bakuhantam.data.repository

import com.polsri.bakuhantam.data.database.dao.PertandinganDao
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import kotlinx.coroutines.flow.Flow

class PertandinganRepository(
    private val dao: PertandinganDao
) {
    val pertandinganList: Flow<List<Pertandingan>> = dao.getAll()

    suspend fun insert(pertandingan: Pertandingan): Long =
        dao.insert(pertandingan)

    suspend fun update(pertandingan: Pertandingan) =
        dao.update(pertandingan)

    suspend fun getById(id: Int): Pertandingan? =
        dao.getById(id)
}
