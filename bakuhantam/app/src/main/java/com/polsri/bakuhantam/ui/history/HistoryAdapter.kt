package com.polsri.bakuhantam.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Pertandingan

class HistoryAdapter :
    ListAdapter<Pertandingan, HistoryAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvMatchTitle)
        private val tvScore = itemView.findViewById<TextView>(R.id.tvMatchScore)

        // ✅ TAMBAHAN (tidak mengubah yang lama)
        private val tvCatatan = itemView.findViewById<TextView>(R.id.tvCatatanWasit)

        fun bind(p: Pertandingan) {
            tvTitle.text = "Atlet ${p.idAtletA} vs Atlet ${p.idAtletB} (${p.kelas})"
            val sA = p.skorA ?: 0
            val sB = p.skorB ?: 0
            tvScore.text = "Skor: $sA - $sB | Pemenang: ${p.pemenang ?: "-"}"

            // ✅ TAMBAHAN (tampil catatan wasit)
            tvCatatan.text = "Catatan Wasit: ${p.catatanWasit ?: "-"}"
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Pertandingan>() {
            override fun areItemsTheSame(oldItem: Pertandingan, newItem: Pertandingan) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Pertandingan, newItem: Pertandingan) = oldItem == newItem
        }
    }
}
