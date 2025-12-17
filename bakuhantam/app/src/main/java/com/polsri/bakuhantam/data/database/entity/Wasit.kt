package com.polsri.bakuhantam.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wasit")
data class Wasit(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama: String,
    val nim: String,
    val prodi: String,
    val fotoUri: String? = null,
    val jumlahMengawasi: Int = 0
)
