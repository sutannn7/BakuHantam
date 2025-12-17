package com.polsri.bakuhantam.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.polsri.bakuhantam.data.database.dao.AtletDao
import com.polsri.bakuhantam.data.database.dao.PertandinganDao
import com.polsri.bakuhantam.data.database.dao.WasitDao
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import com.polsri.bakuhantam.data.database.entity.Wasit

@Database(
    entities = [Atlet::class, Wasit::class, Pertandingan::class],
    version = 2, // dinaikkan dari 1 -> 2
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun atletDao(): AtletDao
    abstract fun wasitDao(): WasitDao
    abstract fun pertandinganDao(): PertandinganDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bakuhantam_db"
                )
                    .fallbackToDestructiveMigration() // kalau struktur berubah, db direset
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
