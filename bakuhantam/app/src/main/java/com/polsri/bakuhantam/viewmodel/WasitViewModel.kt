package com.polsri.bakuhantam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.database.entity.Wasit
import com.polsri.bakuhantam.data.repository.WasitRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WasitViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = WasitRepository(AppDatabase.getInstance(application))
    val wasitList: StateFlow<List<Wasit>> =
        repo.wasitList.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun insert(wasit: Wasit) = viewModelScope.launch { repo.insert(wasit) }
}
