package com.polsri.bakuhantam.ui.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Atlet

class LeaderboardAdapter(
    private val onClick: (Atlet) -> Unit
) : RecyclerView.Adapter<LeaderboardAdapter.VH>() {

    private val data = mutableListOf<Atlet>()

    fun submitList(list: List<Atlet>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvRank: TextView = v.findViewById(R.id.tvRank)
        val tvNama: TextView = v.findViewById(R.id.tvNama)
        val tvStat: TextView = v.findViewById(R.id.tvStat)
        val tvPoin: TextView = v.findViewById(R.id.tvPoin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val a = data[position]
        h.tvRank.text = (position + 1).toString()
        h.tvNama.text = a.nama
        h.tvStat.text = "W:${a.menang} L:${a.kalah} D:${a.seri} | Match:${a.totalPertandingan}"
        h.tvPoin.text = "${a.poin} pts"

        h.itemView.setOnClickListener { onClick(a) }
    }

    override fun getItemCount(): Int = data.size
}
