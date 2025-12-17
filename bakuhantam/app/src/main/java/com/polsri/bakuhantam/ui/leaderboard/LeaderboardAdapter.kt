package com.polsri.bakuhantam.ui.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polsri.bakuhantam.data.database.entity.Atlet

class LeaderboardAdapter :
    ListAdapter<Atlet, LeaderboardAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val text1 = itemView.findViewById<TextView>(android.R.id.text1)
        private val text2 = itemView.findViewById<TextView>(android.R.id.text2)

        fun bind(a: Atlet) {
            text1.text = "${a.nama} (${a.kelas})"
            text2.text =
                "Main: ${a.totalPertandingan} | W:${a.menang} K:${a.kalah} S:${a.seri} | Poin:${a.poin}"
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Atlet>() {
            override fun areItemsTheSame(oldItem: Atlet, newItem: Atlet) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Atlet, newItem: Atlet) = oldItem == newItem
        }
    }
}
