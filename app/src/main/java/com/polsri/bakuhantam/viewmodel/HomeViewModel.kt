package com.polsri.bakuhantam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.repository.AtletRepository
import com.polsri.bakuhantam.data.repository.PertandinganRepository
import java.util.Calendar

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val atletRepo: AtletRepository
    private val pertandinganRepo: PertandinganRepository

    init {
        val db = AppDatabase.getInstance(application)
        atletRepo = AtletRepository(db.atletDao())
        pertandinganRepo = PertandinganRepository(db.pertandinganDao())
    }

    // Data untuk ditampilkan di UI
    val totalAtlet = atletRepo.countAtletFlow().asLiveData()
    val totalPertandingan = pertandinganRepo.countTotalMatchFlow().asLiveData()

    private fun getTodayStartEndMillis(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val end = cal.timeInMillis - 1
        return start to end
    }

    val totalPertandinganHariIni = run {
        val (todayStart, todayEnd) = getTodayStartEndMillis()
        pertandinganRepo.countMatchTodayFlow(todayStart, todayEnd).asLiveData()
    }
}
