package com.polsri.bakuhantam.data.database.dao

import androidx.room.*
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import kotlinx.coroutines.flow.Flow

@Dao
interface PertandinganDao {

    @Insert
    suspend fun insert(data: Pertandingan): Long

    @Update
    suspend fun update(data: Pertandingan)

    // nggak pakai kolom 'tanggal' lagi, pakai id saja
    @Query("SELECT * FROM pertandingan ORDER BY id DESC")
    fun getAll(): Flow<List<Pertandingan>>

    @Query("SELECT * FROM pertandingan WHERE id = :id")
    suspend fun getById(id: Int): Pertandingan?
}
