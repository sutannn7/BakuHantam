package com.polsri.bakuhantam.data.database.dao

import androidx.room.*
import com.polsri.bakuhantam.data.database.entity.Atlet
import kotlinx.coroutines.flow.Flow

@Dao
interface AtletDao {

    @Insert
    suspend fun insert(atlet: Atlet): Long

    @Update
    suspend fun update(atlet: Atlet)

    @Delete
    suspend fun delete(atlet: Atlet)

    // ✅ tadinya ORDER BY poin (padahal kolom poin nggak ada)
    @Query("SELECT * FROM atlet ORDER BY id DESC")
    fun getAll(): Flow<List<Atlet>>

    @Query("SELECT * FROM atlet WHERE id = :id")
    suspend fun getById(id: Int): Atlet?

    @Query("SELECT COUNT(*) FROM atlet")
    suspend fun countAtlet(): Int

    @Query("SELECT COUNT(*) FROM atlet")
    fun countAtletFlow(): Flow<Int>

    @Query("""
    SELECT * FROM atlet 
    ORDER BY poin DESC, menang DESC, totalPertandingan DESC, nama ASC
""")
    fun getLeaderboard(): Flow<List<Atlet>>

}
