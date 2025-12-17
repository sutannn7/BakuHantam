package com.polsri.bakuhantam.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.polsri.bakuhantam.data.database.AppDatabase
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import com.polsri.bakuhantam.data.repository.AtletRepository
import com.polsri.bakuhantam.data.repository.PertandinganRepository
import kotlinx.coroutines.launch
import java.util.Stack

class MatchRoomViewModel(application: Application) : AndroidViewModel(application) {

    private val pertandinganRepo: PertandinganRepository
    private val atletRepo: AtletRepository

    // STATE
    private val _pertandingan = MutableLiveData<Pertandingan?>()
    val pertandingan: LiveData<Pertandingan?> = _pertandingan
    private val _skorA = MutableLiveData(0)
    val skorA: LiveData<Int> = _skorA
    private val _skorB = MutableLiveData(0)
    val skorB: LiveData<Int> = _skorB
    private val _ronde = MutableLiveData(1)
    val ronde: LiveData<Int> = _ronde
    private val _fase = MutableLiveData(Fase.RONDE)
    val fase: LiveData<Fase> = _fase
    private val _peringatanA = MutableLiveData(0)
    val peringatanA: LiveData<Int> = _peringatanA
    private val _peringatanB = MutableLiveData(0)
    val peringatanB: LiveData<Int> = _peringatanB
    private val _pelanggaranA = MutableLiveData(0)
    val pelanggaranA: LiveData<Int> = _pelanggaranA
    private val _pelanggaranB = MutableLiveData(0)
    val pelanggaranB: LiveData<Int> = _pelanggaranB
    private var timer: CountDownTimer? = null
    private val _sisaWaktu = MutableLiveData(DURASI_RONDE)
    val sisaWaktu: LiveData<Long> = _sisaWaktu
    private val _isTimerRunning = MutableLiveData(false)
    val isTimerRunning: LiveData<Boolean> = _isTimerRunning
    private val undoStack = Stack<UndoAction>()
    private val _bisaUndo = MutableLiveData(false)
    val bisaUndo: LiveData<Boolean> = _bisaUndo

    private val _finishEvent = MutableLiveData(false)
    val finishEvent: LiveData<Boolean> = _finishEvent

    init {
        val db = AppDatabase.getInstance(application)
        pertandinganRepo = PertandinganRepository(db.pertandinganDao())
        atletRepo = AtletRepository(db.atletDao())
    }

    fun muatPertandingan(id: Int) {
        viewModelScope.launch {
            _pertandingan.value = pertandinganRepo.getById(id)
            resetState()
        }
    }

    fun onTombolSelesaiDitekan(pemenang: String, catatan: String) {
        viewModelScope.launch {
            val p = _pertandingan.value ?: return@launch
            val atletA = atletRepo.getById(p.idAtletA) ?: return@launch
            val atletB = atletRepo.getById(p.idAtletB) ?: return@launch

            val updatedPertandingan = p.copy(
                skorA = _skorA.value ?: 0,
                skorB = _skorB.value ?: 0,
                pemenang = pemenang,
                catatanWasit = catatan
            )
            pertandinganRepo.update(updatedPertandingan)

            updateAtletStats(atletA, atletB, pemenang)

            _finishEvent.value = true
        }
    }

    private suspend fun updateAtletStats(atletA: Atlet, atletB: Atlet, pemenang: String) {
        atletA.totalPertandingan += 1
        atletB.totalPertandingan += 1

        when (pemenang) {
            "A" -> {
                atletA.menang += 1
                atletB.kalah += 1
                atletA.poin += 3
            }
            "B" -> {
                atletB.menang += 1
                atletA.kalah += 1
                atletB.poin += 3
            }
            "Seri" -> {
                atletA.seri += 1
                atletB.seri += 1
                atletA.poin += 1
                atletB.poin += 1
            }
        }

        atletRepo.update(atletA)
        atletRepo.update(atletB)
    }
    
    fun onTombolPlusADitekan() {
        _skorA.value = (_skorA.value ?: 0) + 1
        tambahAksiUndo(UndoAction.SKOR_A)
    }

    fun onTombolPlusBDitekan() {
        _skorB.value = (_skorB.value ?: 0) + 1
        tambahAksiUndo(UndoAction.SKOR_B)
    }

    fun onTombolPeringatanADitekan() {
        _peringatanA.value = (_peringatanA.value ?: 0) + 1
        tambahAksiUndo(UndoAction.PERINGATAN_A)
    }

    fun onTombolPeringatanBDitekan() {
        _peringatanB.value = (_peringatanB.value ?: 0) + 1
        tambahAksiUndo(UndoAction.PERINGATAN_B)
    }

    fun onTombolPelanggaranADitekan() {
        _pelanggaranA.value = (_pelanggaranA.value ?: 0) + 1
        tambahAksiUndo(UndoAction.PELANGGARAN_A)
    }

    fun onTombolPelanggaranBDitekan() {
        _pelanggaranB.value = (_pelanggaranB.value ?: 0) + 1
        tambahAksiUndo(UndoAction.PELANGGARAN_B)
    }

    fun onTombolUndoDitekan() {
        if (undoStack.isNotEmpty()) {
            when (undoStack.pop()) {
                UndoAction.SKOR_A -> _skorA.value = (_skorA.value ?: 0) - 1
                UndoAction.SKOR_B -> _skorB.value = (_skorB.value ?: 0) - 1
                UndoAction.PERINGATAN_A -> _peringatanA.value = (_peringatanA.value ?: 0) - 1
                UndoAction.PERINGATAN_B -> _peringatanB.value = (_peringatanB.value ?: 0) - 1
                UndoAction.PELANGGARAN_A -> _pelanggaranA.value = (_pelanggaranA.value ?: 0) - 1
                UndoAction.PELANGGARAN_B -> _pelanggaranB.value = (_pelanggaranB.value ?: 0) - 1
            }
        }
        _bisaUndo.value = undoStack.isNotEmpty()
    }

    fun onTombolStartPauseDitekan() {
        if (_isTimerRunning.value == true) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    fun onTombolResetDitekan() {
        resetState()
    }

    fun onTombolNextDitekan() {
        stopTimer()
        if(_fase.value == Fase.RONDE) {
            _fase.value = Fase.ISTIRAHAT
            _sisaWaktu.value = DURASI_ISTIRAHAT
        } else if ((_ronde.value ?: 0) < MAX_RONDE) {
            _ronde.value = (_ronde.value ?: 0) + 1
            _fase.value = Fase.RONDE
            _sisaWaktu.value = DURASI_RONDE
        }
    }

    fun onFinishEventHandled() {
        _finishEvent.value = false
    }

    private fun startTimer() {
        _isTimerRunning.value = true
        timer = object : CountDownTimer(_sisaWaktu.value ?: 0, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _sisaWaktu.value = millisUntilFinished
            }

            override fun onFinish() {
                _isTimerRunning.value = false
                onTombolNextDitekan()
            }
        }.start()
    }

    private fun stopTimer() {
        timer?.cancel()
        _isTimerRunning.value = false
    }

    private fun resetState() {
        stopTimer()
        _skorA.value = _pertandingan.value?.skorA ?: 0
        _skorB.value = _pertandingan.value?.skorB ?: 0
        _ronde.value = 1
        _fase.value = Fase.RONDE
        _sisaWaktu.value = DURASI_RONDE
        _peringatanA.value = 0
        _peringatanB.value = 0
        _pelanggaranA.value = 0
        _pelanggaranB.value = 0
        undoStack.clear()
        _bisaUndo.value = false
        _finishEvent.value = false
    }

    private fun tambahAksiUndo(aksi: UndoAction) {
        undoStack.push(aksi)
        _bisaUndo.value = true
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val DURASI_RONDE = 3 * 60 * 1000L
        private const val DURASI_ISTIRAHAT = 60 * 1000L
        private const val MAX_RONDE = 3
    }

    private enum class UndoAction { SKOR_A, SKOR_B, PERINGATAN_A, PERINGATAN_B, PELANGGARAN_A, PELANGGARAN_B }
    enum class Fase { RONDE, ISTIRAHAT }
}
