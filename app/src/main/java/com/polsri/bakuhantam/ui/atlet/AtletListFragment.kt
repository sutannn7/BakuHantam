package com.polsri.bakuhantam.ui.atlet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.viewmodel.AtletViewModel
import java.util.Locale
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AtletListFragment : Fragment(R.layout.fragment_atlet_list) {

    private val atletVm: AtletViewModel by activityViewModels()
    private lateinit var adapter: AtletAdapter

    private var fullList: List<Atlet> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<EditText>(R.id.etSearchAtlet)
        val rv = view.findViewById<RecyclerView>(R.id.rvAtlet)
        val tvEmpty = view.findViewById<TextView>(R.id.tvEmptyAtlet)
        val fab = view.findViewById<FloatingActionButton>(R.id.fabAddAtlet)
        fab.setOnClickListener {
            findNavController().navigate(R.id.tambahAtletFragment)
        }

        adapter = AtletAdapter { atlet ->
            val b = Bundle().apply { putInt("atletId", atlet.id) }
            findNavController().navigate(R.id.atletDetailFragment, b)
        }

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // Observe data atlet
        atletVm.atletList.observe(viewLifecycleOwner) { list ->
            fullList = list ?: emptyList()
            renderList(fullList, tvEmpty)
        }

        // Search
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val q = (s?.toString() ?: "").trim().lowercase(Locale.getDefault())
                val filtered = if (q.isEmpty()) fullList else fullList.filter {
                    it.nama.lowercase(Locale.getDefault()).contains(q) ||
                            it.nim.lowercase(Locale.getDefault()).contains(q)
                }
                renderList(filtered, tvEmpty)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun renderList(list: List<Atlet>, tvEmpty: TextView) {
        if (list.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
        } else {
            tvEmpty.visibility = View.GONE
        }
        adapter.submitList(list)
    }
}
