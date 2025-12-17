package com.polsri.bakuhantam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.polsri.bakuhantam.data.database.entity.Wasit
import kotlinx.coroutines.flow.Flow

@Dao
interface WasitDao {

    @Insert
    suspend fun insert(wasit: Wasit): Long

    @Query("SELECT * FROM wasit")
    fun getAll(): Flow<List<Wasit>>
}
