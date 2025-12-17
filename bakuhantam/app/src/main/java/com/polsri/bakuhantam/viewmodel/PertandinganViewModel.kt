package com.polsri.bakuhantam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PertandinganViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val pertandinganDao = db.pertandinganDao()
    private val atletDao = db.atletDao()

    // dipakai HistoryFragment
    val pertandinganList: Flow<List<Pertandingan>> = pertandinganDao.getAll()

    // dipakai BuatPertandinganFragment (buat pertandingan baru, return id)
    suspend fun buatPertandinganReturnId(pertandingan: Pertandingan): Long {
        return pertandinganDao.insert(pertandingan)
    }

    // dipakai MatchRoomFragment
    suspend fun getById(id: Int): Pertandingan? {
        return pertandinganDao.getById(id)
    }

    // dipakai MatchRoomFragment saat klik simpan hasil
    fun simpanHasil(pertandinganBaru: Pertandingan) {
        viewModelScope.launch {
            // update pertandingan
            pertandinganDao.update(pertandinganBaru)

            // update statistik atlet
            val atletA = atletDao.getById(pertandinganBaru.idAtletA)
            val atletB = atletDao.getById(pertandinganBaru.idAtletB)

            if (atletA != null && atletB != null) {
                when (pertandinganBaru.pemenang) {
                    "A" -> {
                        updateAtlet(
                            atlet = atletA,
                            tambahMenang = 1,
                            tambahPoin = 3
                        )
                        updateAtlet(
                            atlet = atletB,
                            tambahKalah = 1
                        )
                    }

                    "B" -> {
                        updateAtlet(
                            atlet = atletB,
                            tambahMenang = 1,
                            tambahPoin = 3
                        )
                        updateAtlet(
                            atlet = atletA,
                            tambahKalah = 1
                        )
                    }

                    "Seri" -> {
                        updateAtlet(
                            atlet = atletA,
                            tambahSeri = 1,
                            tambahPoin = 1
                        )
                        updateAtlet(
                            atlet = atletB,
                            tambahSeri = 1,
                            tambahPoin = 1
                        )
                    }
                }
            }
        }
    }

    private suspend fun updateAtlet(
        atlet: Atlet,
        tambahMenang: Int = 0,
        tambahKalah: Int = 0,
        tambahSeri: Int = 0,
        tambahPoin: Int = 0
    ) {
        val updated = atlet.copy(
            totalPertandingan = atlet.totalPertandingan + 1,
            menang = atlet.menang + tambahMenang,
            kalah = atlet.kalah + tambahKalah,
            seri = atlet.seri + tambahSeri,
            poin = atlet.poin + tambahPoin
        )
        atletDao.update(updated)
    }
}
