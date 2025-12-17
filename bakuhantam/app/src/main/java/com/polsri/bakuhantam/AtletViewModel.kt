package com.polsri.bakuhantam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.data.repository.AtletRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AtletViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AtletRepository(AppDatabase.getInstance(application))
    val atletList: StateFlow<List<Atlet>> =
        repo.atletList.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun insert(atlet: Atlet) = viewModelScope.launch { repo.insert(atlet) }
    fun update(atlet: Atlet) = viewModelScope.launch { repo.update(atlet) }
    suspend fun getById(id: Int) = repo.getById(id)
}
