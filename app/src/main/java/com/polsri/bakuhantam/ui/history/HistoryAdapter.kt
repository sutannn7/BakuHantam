package com.polsri.bakuhantam.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val onClick: (Pertandingan) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.VH>() {

    private val data = mutableListOf<Pertandingan>()

    fun submit(list: List<Pertandingan>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById(R.id.tvMatchTitle)
        val tvScore: TextView = v.findViewById(R.id.tvMatchScore)
        val tvDate: TextView = v.findViewById(R.id.tvMatchDate)
        val tvResult: TextView = v.findViewById(R.id.tvMatchResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(h: VH, position: Int) {
        val p = data[position]

        h.tvTitle.text = "Match #${p.id}"

        // Karena entity pertandingan pakai idAtletA / idAtletB (bukan namaAtletA/B)
        h.tvScore.text = "Atlet #${p.idAtletA} ${p.skorA} : ${p.skorB} Atlet #${p.idAtletB}"

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        h.tvDate.text = sdf.format(Date(p.tanggal))

        h.tvResult.text = if (p.pemenang.isBlank()) {
            "Belum ada pemenang"
        } else {
            "Pemenang: ${p.pemenang}"
        }

        h.itemView.setOnClickListener { onClick(p) }
    }
}
