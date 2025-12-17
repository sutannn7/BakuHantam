package com.polsri.bakuhantam.ui.live

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R

class LiveFragment : Fragment(R.layout.fragment_live) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvLastMatchInfo = view.findViewById<TextView>(R.id.tvLastMatchInfo)
        val btnResume = view.findViewById<Button>(R.id.btnResume)
        val btnBuatMatch = view.findViewById<Button>(R.id.btnBuatMatch)
        val btnKeHome = view.findViewById<Button>(R.id.btnKeHome)

        val prefs = requireContext().getSharedPreferences("app_state", 0)
        val lastMatchId = prefs.getInt("lastMatchId", -1)

        if (lastMatchId > 0) {
            tvLastMatchInfo.text = "Match ID: #$lastMatchId"
            btnResume.isEnabled = true
        } else {
            tvLastMatchInfo.text = "Belum ada match yang dibuka."
            btnResume.isEnabled = false
        }

        btnResume.setOnClickListener {
            if (lastMatchId <= 0) {
                Toast.makeText(requireContext(), "Belum ada match untuk di-resume", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val b = Bundle().apply {
                putInt("matchId", lastMatchId)
                putInt("pertandinganId", lastMatchId) // kompatibel
            }
            findNavController().navigate(R.id.matchRoomFragment, b)
        }

        btnBuatMatch.setOnClickListener {
            findNavController().navigate(R.id.buatPertandinganFragment)
        }

        btnKeHome.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }
}
