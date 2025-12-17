package com.polsri.bakuhantam.ui.pertandingan

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.viewmodel.PertandinganViewModel
import kotlinx.coroutines.launch

class MatchSummaryFragment : Fragment(R.layout.fragment_match_summary) {

    private val pVm: PertandinganViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvHeader = view.findViewById<TextView>(R.id.tvHeader)
        val tvScore = view.findViewById<TextView>(R.id.tvScore)
        val tvWinner = view.findViewById<TextView>(R.id.tvWinner)
        val tvRonde = view.findViewById<TextView>(R.id.tvRonde)
        val tvPenalty = view.findViewById<TextView>(R.id.tvPenalty)
        val tvCatatan = view.findViewById<TextView>(R.id.tvCatatan)
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        val matchId = arguments?.getInt("matchId") ?: -1

        viewLifecycleOwner.lifecycleScope.launch {
            val p = pVm.getById(matchId)
            if (p == null) {
                tvHeader.text = "Match tidak ditemukan"
                return@launch
            }

            tvHeader.text = "Atlet ${p.idAtletA} vs Atlet ${p.idAtletB}"
            tvScore.text = "${p.skorA} - ${p.skorB}"
            tvWinner.text = "Pemenang: ${if (p.pemenang.isBlank()) "-" else p.pemenang}"

            // Ini akan tampil dari catatanWasit yang sudah kamu tempel info ronde+penalty
            tvRonde.text = "Ronde: lihat catatan"
            tvPenalty.text = "Warning/Foul: lihat catatan"

            tvCatatan.text = "Catatan:\n${p.catatanWasit ?: "-"}"
        }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
