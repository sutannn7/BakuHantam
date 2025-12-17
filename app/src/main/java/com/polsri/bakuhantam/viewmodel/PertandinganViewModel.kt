package com.polsri.bakuhantam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import kotlinx.coroutines.flow.Flow

class PertandinganViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val pertandinganDao = db.pertandinganDao()

    val pertandinganList: Flow<List<Pertandingan>> = pertandinganDao.getAll()

    suspend fun buatPertandinganReturnId(pertandingan: Pertandingan): Long {
        return pertandinganDao.insert(pertandingan)
    }

    suspend fun getById(id: Int): Pertandingan? {
        return pertandinganDao.getById(id)
    }

    fun simpanHasil(pertandinganBaru: Pertandingan) {
        // kamu sudah punya versi simpanHasil kamu, biarkan sesuai yang terakhir kamu pakai
        // (kalau mau aku satukan dengan anti dobel hitung, bilang)
        // placeholder:
        // viewModelScope.launch { pertandinganDao.update(pertandinganBaru) }
    }

    // ✅ BARU: dipakai AtletDetailFragment
    fun getHistoryByAtlet(atletId: Int): Flow<List<Pertandingan>> {
        return pertandinganDao.getHistoryByAtlet(atletId)
    }
}
