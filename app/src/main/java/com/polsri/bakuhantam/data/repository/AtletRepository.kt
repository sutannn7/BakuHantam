package com.polsri.bakuhantam.data.repository

import com.polsri.bakuhantam.data.database.dao.AtletDao
import com.polsri.bakuhantam.data.database.entity.Atlet
import kotlinx.coroutines.flow.Flow

class AtletRepository(private val atletDao: AtletDao) {

    val atletFlow: Flow<List<Atlet>> = atletDao.getAll()

    suspend fun insert(atlet: Atlet) = atletDao.insert(atlet)
    suspend fun update(atlet: Atlet) = atletDao.update(atlet)
    suspend fun delete(atlet: Atlet) = atletDao.delete(atlet)

    suspend fun getById(id: Int): Atlet? = atletDao.getById(id)

    fun countAtletFlow() = atletDao.countAtletFlow()
}
