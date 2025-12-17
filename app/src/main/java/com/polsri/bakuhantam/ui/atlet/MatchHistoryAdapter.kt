package com.polsri.bakuhantam.ui.atlet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polsri.bakuhantam.R

data class MatchHistoryUi(
    val matchId: Int,
    val title: String,
    val sub: String
)

class MatchHistoryAdapter(
    private val onClick: (MatchHistoryUi) -> Unit
) : RecyclerView.Adapter<MatchHistoryAdapter.VH>() {

    private val data = mutableListOf<MatchHistoryUi>()

    fun submitList(list: List<MatchHistoryUi>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById(R.id.tvMatchTitle)
        val tvSub: TextView = v.findViewById(R.id.tvMatchSub)
        val tvId: TextView = v.findViewById(R.id.tvMatchId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match_history, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val item = data[position]
        h.tvTitle.text = item.title
        h.tvSub.text = item.sub
        h.tvId.text = "Match #${item.matchId}"

        h.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = data.size
}
