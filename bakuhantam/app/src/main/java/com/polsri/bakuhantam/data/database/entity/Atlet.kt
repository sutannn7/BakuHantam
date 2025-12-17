package com.polsri.bakuhantam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "atlet")
data class Atlet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val nama: String,
    val nim: String,
    val prodi: String,
    val tahunMasuk: Int,

    val fotoUri: String? = null,

    val berat: Float,
    val tinggi: Float,
    val kelas: String,
    val bmi: Float,

    val pengalaman: Int? = 0,
    val riwayatCedera: String? = null,

    // statistik untuk leaderboard
    val totalPertandingan: Int = 0,
    val menang: Int = 0,
    val kalah: Int = 0,
    val seri: Int = 0,
    val poin: Int = 0
)
