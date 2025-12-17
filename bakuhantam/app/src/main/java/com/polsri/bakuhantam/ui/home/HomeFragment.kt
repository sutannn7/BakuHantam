package com.polsri.bakuhantam.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnTambahAtlet).setOnClickListener {
            findNavController().navigate(R.id.tambahAtletFragment) // pastikan ID ini ada di nav_graph
        }

        view.findViewById<Button>(R.id.btnBuatPertandingan).setOnClickListener {
            findNavController().navigate(R.id.buatPertandinganFragment) // pastikan ID ini ada di nav_graph
        }
    }
}
