package com.polsri.bakuhantam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pertandingan")
data class Pertandingan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val kelas: String,
    val idAtletA: Int,
    val idAtletB: Int,
    val idWasit: Int,

    val skorA: Int = 0,
    val skorB: Int = 0,
    val pemenang: String = "",

    // catatan wasit
    val catatanWasit: String? = null,

    // ✅ TAMBAHAN (UNTUK STATISTIK HARI INI)
    val tanggal: Long
)
