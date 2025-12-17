package com.polsri.bakuhantam.ui.leaderboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.ChipGroup
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.AppDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroup)

        val tv1Name = view.findViewById<TextView>(R.id.tvPodium1Name)
        val tv1Score = view.findViewById<TextView>(R.id.tvPodium1Score)
        val tv2Name = view.findViewById<TextView>(R.id.tvPodium2Name)
        val tv2Score = view.findViewById<TextView>(R.id.tvPodium2Score)
        val tv3Name = view.findViewById<TextView>(R.id.tvPodium3Name)
        val tv3Score = view.findViewById<TextView>(R.id.tvPodium3Score)

        val tvRankList = view.findViewById<TextView>(R.id.tvRankList)

        val atletDao = AppDatabase.getInstance(requireContext()).atletDao()

        // ✅ Pakai DAO asli kamu: getLeaderboard(): Flow<List<Atlet>>
        viewLifecycleOwner.lifecycleScope.launch {
            atletDao.getLeaderboard().collectLatest { list ->

                fun setPodium(index: Int, name: TextView, score: TextView) {
                    if (list.size > index) {
                        val a = list[index]
                        name.text = a.nama
                        score.text = "${a.poin} poin"
                    } else {
                        name.text = "(kosong)"
                        score.text = "0 poin"
                    }
                }

                // Podium
                setPodium(0, tv1Name, tv1Score)
                setPodium(1, tv2Name, tv2Score)
                setPodium(2, tv3Name, tv3Score)

                // List Ranking (tampilkan semua atau batasi 10)
                if (list.isEmpty()) {
                    tvRankList.text = "Belum ada data atlet."
                } else {
                    val sb = StringBuilder()
                    list.forEachIndexed { idx, a ->
                        sb.append("${idx + 1}. ${a.nama} — ${a.poin} poin\n")
                    }
                    tvRankList.text = sb.toString().trim()
                }
            }
        }

        // Filter UI dulu (logic bisa nyusul)
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipAll -> Toast.makeText(requireContext(), "All Time", Toast.LENGTH_SHORT).show()
                R.id.chipMonth -> Toast.makeText(requireContext(), "Bulan Ini (coming soon)", Toast.LENGTH_SHORT).show()
                R.id.chipWeek -> Toast.makeText(requireContext(), "Minggu Ini (coming soon)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
