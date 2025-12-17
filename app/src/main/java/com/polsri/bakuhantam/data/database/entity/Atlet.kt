package com.polsri.bakuhantam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "atlet")
data class Atlet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nama: String,

    // ✅ Tambahan data mahasiswa (biar error hilang)
    val nim: String,
    val prodi: String,
    val tahunMasuk: Int,
    val fotoUri: String? = null,

    // ✅ Data fisik
    val berat: Float,
    val tinggi: Float,

    // ✅ Kelas & BMI (kamu pakai ini)
    val kelas: String,
    val bmi: Float,

    // ✅ Riwayat
    val pengalaman: Int,
    val riwayatCedera: String? = null,

    // ✅ Statistik default (biar aman untuk leaderboard)
    var totalPertandingan: Int = 0,
    var menang: Int = 0,
    var kalah: Int = 0,
    var seri: Int = 0,
    var poin: Int = 0
)
