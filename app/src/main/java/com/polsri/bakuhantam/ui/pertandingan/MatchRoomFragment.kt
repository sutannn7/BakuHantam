package com.polsri.bakuhantam.ui.pertandingan

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.viewmodel.MatchRoomViewModel
import java.util.concurrent.TimeUnit

class MatchRoomFragment : Fragment(R.layout.fragment_match_room) {

    private val viewModel: MatchRoomViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pertandinganId = arguments?.getInt("pertandinganId") ?: -1
        if (pertandinganId != -1) {
            viewModel.muatPertandingan(pertandinganId)
        }

        val rgWinner = view.findViewById<RadioGroup>(R.id.rgWinner)
        val etCatatan = view.findViewById<EditText>(R.id.etCatatan)
        val btnSimpanHasil = view.findViewById<Button>(R.id.btnSimpanHasil)

        view.findViewById<Button>(R.id.btnPlusA).setOnClickListener { viewModel.onTombolPlusADitekan() }
        view.findViewById<Button>(R.id.btnPlusB).setOnClickListener { viewModel.onTombolPlusBDitekan() }
        view.findViewById<Button>(R.id.btnStartPause).setOnClickListener { viewModel.onTombolStartPauseDitekan() }
        view.findViewById<Button>(R.id.btnResetTimer).setOnClickListener { viewModel.onTombolResetDitekan() }
        view.findViewById<Button>(R.id.btnNextPhase).setOnClickListener { viewModel.onTombolNextDitekan() }
        view.findViewById<Button>(R.id.btnUndo).setOnClickListener { viewModel.onTombolUndoDitekan() }
        view.findViewById<Button>(R.id.btnWarningA).setOnClickListener { viewModel.onTombolPeringatanADitekan() }
        view.findViewById<Button>(R.id.btnWarningB).setOnClickListener { viewModel.onTombolPeringatanBDitekan() }
        view.findViewById<Button>(R.id.btnFoulA).setOnClickListener { viewModel.onTombolPelanggaranADitekan() }
        view.findViewById<Button>(R.id.btnFoulB).setOnClickListener { viewModel.onTombolPelanggaranBDitekan() }

        btnSimpanHasil.setOnClickListener {
            val selectedWinnerId = rgWinner.checkedRadioButtonId
            val pemenang = when (selectedWinnerId) {
                R.id.rbA -> "A"
                R.id.rbB -> "B"
                R.id.rbSeri -> "Seri"
                else -> {
                    Toast.makeText(context, "Pilih pemenang terlebih dahulu", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val catatan = etCatatan.text.toString()
            viewModel.onTombolSelesaiDitekan(pemenang, catatan)
        }

        observeViewModel(view)
    }

    private fun observeViewModel(view: View) {
        val tvHeader: TextView = view.findViewById(R.id.tvHeader)
        val tvAtletA: TextView = view.findViewById(R.id.tvAtletA)
        val tvAtletB: TextView = view.findViewById(R.id.tvAtletB)
        val tvScore: TextView = view.findViewById(R.id.tvScore)
        val tvTimer: TextView = view.findViewById(R.id.tvTimer)
        val btnStartPause: Button = view.findViewById(R.id.btnStartPause)
        val tvRoundState: TextView = view.findViewById(R.id.tvRoundState)
        val btnUndo: Button = view.findViewById(R.id.btnUndo)
        val btnWarningA: Button = view.findViewById(R.id.btnWarningA)
        val btnWarningB: Button = view.findViewById(R.id.btnWarningB)
        val btnFoulA: Button = view.findViewById(R.id.btnFoulA)
        val btnFoulB: Button = view.findViewById(R.id.btnFoulB)

        viewModel.pertandingan.observe(viewLifecycleOwner) { p ->
            p?.let {
                tvHeader.text = "Match #${it.id}"
                tvAtletA.text = "Atlet A: ${it.idAtletA}" // TODO: Ganti dengan nama asli
                tvAtletB.text = "Atlet B: ${it.idAtletB}"
            }
        }

        viewModel.skorA.observe(viewLifecycleOwner) { tvScore.text = "${it} - ${viewModel.skorB.value}" }
        viewModel.skorB.observe(viewLifecycleOwner) { tvScore.text = "${viewModel.skorA.value} - ${it}" }

        viewModel.sisaWaktu.observe(viewLifecycleOwner) { sisa ->
            val minutes = TimeUnit.MILLISECONDS.toMinutes(sisa)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(sisa) % 60
            tvTimer.text = String.format("%02d:%02d", minutes, seconds)
        }

        viewModel.isTimerRunning.observe(viewLifecycleOwner) { btnStartPause.text = if (it) "PAUSE" else "START" }
        viewModel.ronde.observe(viewLifecycleOwner) { if (viewModel.fase.value == MatchRoomViewModel.Fase.RONDE) tvRoundState.text = "RONDE $it" }
        viewModel.fase.observe(viewLifecycleOwner) { tvRoundState.text = if (it == MatchRoomViewModel.Fase.RONDE) "RONDE ${viewModel.ronde.value}" else "ISTIRAHAT" }
        viewModel.bisaUndo.observe(viewLifecycleOwner) { btnUndo.isEnabled = it }
        viewModel.peringatanA.observe(viewLifecycleOwner) { btnWarningA.text = "⚠ A ($it)" }
        viewModel.peringatanB.observe(viewLifecycleOwner) { btnWarningB.text = "⚠ B ($it)" }
        viewModel.pelanggaranA.observe(viewLifecycleOwner) { btnFoulA.text = "❌ A ($it)" }
        viewModel.pelanggaranB.observe(viewLifecycleOwner) { btnFoulB.text = "❌ B ($it)" }

        viewModel.finishEvent.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                Toast.makeText(context, "Pertandingan selesai dan disimpan!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack(R.id.homeFragment, false)
                viewModel.onFinishEventHandled() // Reset event to prevent re-triggering
            }
        }
    }
}
