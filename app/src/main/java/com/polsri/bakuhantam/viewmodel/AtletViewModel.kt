package com.polsri.bakuhantam.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.data.repository.AtletRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AtletViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repo = AtletRepository(db.atletDao())

    val atletList = repo.atletFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    ).asLiveData()

    fun insert(atlet: Atlet) {
        viewModelScope.launch { repo.insert(atlet) }
    }

    fun update(atlet: Atlet) {
        viewModelScope.launch { repo.update(atlet) }
    }

    fun delete(atlet: Atlet) {
        viewModelScope.launch { repo.delete(atlet) }
    }

    suspend fun getById(id: Int): Atlet? = repo.getById(id)
}
