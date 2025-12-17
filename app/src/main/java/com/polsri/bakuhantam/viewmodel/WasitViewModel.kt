package com.polsri.bakuhantam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.database.entity.Wasit
import com.polsri.bakuhantam.data.repository.WasitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WasitViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = WasitRepository(AppDatabase.getInstance(application))
    val wasitList = repo.wasitList.asLiveData()

    init {
        // Pre-load the list to ensure it's available early
        viewModelScope.launch {
            repo.wasitList.first()
        }
    }

    fun insert(wasit: Wasit) = viewModelScope.launch { repo.insert(wasit) }
}
