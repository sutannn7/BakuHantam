package com.polsri.bakuhantam.ui.atlet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Atlet

class AtletAdapter(
    private val onClick: (Atlet) -> Unit
) : ListAdapter<Atlet, AtletAdapter.VH>(AtletDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_atlet, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val a = getItem(position)

        h.tvNama.text = a.nama
        h.tvSub.text = "${a.nim} • ${a.prodi} • ${a.kelas}"
        h.tvStat.text =
            "Poin: ${a.poin} • W:${a.menang} L:${a.kalah} D:${a.seri} • Match:${a.totalPertandingan}"

        // Avatar huruf pertama
        h.tvAvatar.text = a.nama.take(1).uppercase()

        h.itemView.setOnClickListener { onClick(a) }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvNama: TextView = v.findViewById(R.id.tvNama)
        val tvSub: TextView = v.findViewById(R.id.tvSub)
        val tvStat: TextView = v.findViewById(R.id.tvStat)
        val tvAvatar: TextView = v.findViewById(R.id.tvAvatar)
    }

    // This class helps ListAdapter determine which items have changed
    class AtletDiffCallback : DiffUtil.ItemCallback<Atlet>() {
        override fun areItemsTheSame(oldItem: Atlet, newItem: Atlet): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Atlet, newItem: Atlet): Boolean {
            return oldItem == newItem
        }
    }
}
