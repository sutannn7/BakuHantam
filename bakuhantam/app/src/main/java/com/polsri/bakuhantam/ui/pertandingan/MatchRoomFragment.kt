package com.polsri.bakuhantam.ui.pertandingan

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import com.polsri.bakuhantam.viewmodel.PertandinganViewModel
import kotlinx.coroutines.launch

class MatchRoomFragment : Fragment(R.layout.fragment_match_room) {

    private val pVm: PertandinganViewModel by activityViewModels()

    private var scoreA = 0
    private var scoreB = 0
    private var pertandingan: Pertandingan? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ ambil id pertandingan dari arguments biasa
        val pertandinganId = requireArguments().getInt("pertandinganId", -1)
        if (pertandinganId == -1) {
            Toast.makeText(requireContext(), "ID pertandingan tidak valid", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        }

        val btnPlusA = view.findViewById<Button>(R.id.btnPlusA)
        val btnPlusB = view.findViewById<Button>(R.id.btnPlusB)
        val tvHeader = view.findViewById<TextView>(R.id.tvHeader)
        val tvScore = view.findViewById<TextView>(R.id.tvScore)
        val rg = view.findViewById<RadioGroup>(R.id.rgWinner)
        val etCat = view.findViewById<EditText>(R.id.etCatatan)
        val btnSimpan = view.findViewById<Button>(R.id.btnSimpanHasil)

        viewLifecycleOwner.lifecycleScope.launch {
            pertandingan = pVm.getById(pertandinganId)
            if (pertandingan == null) {
                Toast.makeText(requireContext(), "Pertandingan tidak ditemukan", Toast.LENGTH_SHORT).show()
                return@launch
            }
            tvHeader.text = "Match #$pertandinganId (Kelas: ${pertandingan!!.kelas})"
        }

        btnPlusA.setOnClickListener {
            scoreA++
            tvScore.text = "$scoreA - $scoreB"
        }
        btnPlusB.setOnClickListener {
            scoreB++
            tvScore.text = "$scoreA - $scoreB"
        }

        btnSimpan.setOnClickListener {
            val winner = when (rg.checkedRadioButtonId) {
                R.id.rbA -> "A"
                R.id.rbB -> "B"
                R.id.rbSeri -> "Seri"
                else -> null
            }
            if (winner == null) {
                Toast.makeText(requireContext(), "Pilih pemenang!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pOld = pertandingan
            if (pOld == null) {
                Toast.makeText(requireContext(), "Pertandingan belum siap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pNew = pOld.copy(
                skorA = scoreA,
                skorB = scoreB,
                pemenang = winner,
                catatanWasit = etCat.text.toString()
            )
            pVm.simpanHasil(pNew)
            Toast.makeText(requireContext(), "Hasil disimpan", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
