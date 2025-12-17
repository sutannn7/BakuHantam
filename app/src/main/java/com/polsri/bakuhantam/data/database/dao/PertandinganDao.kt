package com.polsri.bakuhantam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import kotlinx.coroutines.flow.Flow

@Dao
interface PertandinganDao {

    @Insert
    suspend fun insert(data: Pertandingan): Long

    @Update
    suspend fun update(data: Pertandingan)

    @Query("SELECT * FROM pertandingan ORDER BY id DESC")
    fun getAll(): Flow<List<Pertandingan>>

    @Query("SELECT * FROM pertandingan WHERE id = :id")
    suspend fun getById(id: Int): Pertandingan?

    @Query("SELECT COUNT(*) FROM pertandingan")
    fun countTotalMatchFlow(): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM pertandingan
        WHERE tanggal BETWEEN :start AND :end
    """)
    fun countMatchTodayFlow(start: Long, end: Long): Flow<Int>

    // ✅ RIWAYAT PERTANDINGAN PER ATLET
    @Query("""
        SELECT * FROM pertandingan
        WHERE idAtletA = :atletId OR idAtletB = :atletId
        ORDER BY tanggal DESC, id DESC
    """)
    fun getHistoryByAtlet(atletId: Int): Flow<List<Pertandingan>>

    @Query("SELECT * FROM pertandingan ORDER BY tanggal DESC")
    fun getHistory(): kotlinx.coroutines.flow.Flow<List<com.polsri.bakuhantam.data.database.entity.Pertandingan>>

    @Query("SELECT * FROM pertandingan WHERE tanggal BETWEEN :start AND :end ORDER BY tanggal DESC")
    fun getHistoryByDateRange(
        start: Long,
        end: Long
    ): kotlinx.coroutines.flow.Flow<List<com.polsri.bakuhantam.data.database.entity.Pertandingan>>

}
