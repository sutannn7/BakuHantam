package com.polsri.bakuhantam.ui.history

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.AppDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@Suppress("DEPRECATION")
class HistoryFragment : Fragment(R.layout.fragment_history) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvHistory)
        val tvEmpty = view.findViewById<TextView>(R.id.tvHistoryEmpty)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupHistory)

        val adapter = HistoryAdapter { pertandingan ->
            val b = Bundle().apply {
                putInt("matchId", pertandingan.id)
                putInt("pertandinganId", pertandingan.id)
            }
            findNavController().navigate(R.id.matchRoomFragment, b)
        }

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        val dao = AppDatabase.getInstance(requireContext()).pertandinganDao()

        // Default: semua history
        fun loadAll() {
            viewLifecycleOwner.lifecycleScope.launch {
                dao.getHistory().collectLatest { list ->
                    adapter.submit(list)
                    tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        fun loadToday() {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val start = cal.timeInMillis
            cal.add(Calendar.DAY_OF_MONTH, 1)
            val end = cal.timeInMillis - 1

            viewLifecycleOwner.lifecycleScope.launch {
                dao.getHistoryByDateRange(start, end).collectLatest { list ->
                    adapter.submit(list)
                    tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        loadAll()

        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipAllHistory -> loadAll()
                R.id.chipTodayHistory -> loadToday()
            }
        }
    }
}
