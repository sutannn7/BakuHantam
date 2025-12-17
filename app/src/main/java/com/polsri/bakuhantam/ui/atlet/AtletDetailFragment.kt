package com.polsri.bakuhantam.ui.atlet

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.viewmodel.AtletViewModel
import kotlinx.coroutines.launch

class AtletDetailFragment : Fragment(R.layout.fragment_atlet_details) {

    private val atletVm: AtletViewModel by activityViewModels()
    private var atlet: Atlet? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val atletId = requireArguments().getInt("atletId", -1)
        if (atletId == -1) {
            Toast.makeText(requireContext(), "ID atlet tidak valid", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        // Sesuaikan ID TextView kamu di layout detail
        val tvNama = view.findViewById<TextView>(R.id.tvNamaDetail)
        val tvInfo = view.findViewById<TextView>(R.id.tvInfo)

        // ✅ FIX: tombol kembali (di UI kamu ada tombol "KEMBALI")
        val btnBack = view.findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


        val btnEdit = view.findViewById<Button>(R.id.btnEditAtlet)
        val btnHapus = view.findViewById<Button>(R.id.btnHapusAtlet)

        viewLifecycleOwner.lifecycleScope.launch {
            val a = atletVm.getById(atletId)
            if (a == null) {
                Toast.makeText(requireContext(), "Atlet tidak ditemukan", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                return@launch
            }
            atlet = a

            // contoh tampil
            tvNama.text = a.nama
            tvInfo.text = "NIM: ${a.nim}\nProdi: ${a.prodi}\nKelas: ${a.kelas}\nBMI: ${"%.2f".format(a.bmi)}"
        }

        btnEdit.setOnClickListener {
            val b = Bundle().apply { putInt("atletId", atletId) }
            findNavController().navigate(R.id.tambahAtletFragment, b)
        }

        btnHapus.setOnClickListener {
            val current = atlet ?: return@setOnClickListener
            AlertDialog.Builder(requireContext())
                .setTitle("Hapus Atlet?")
                .setMessage("Data atlet akan dihapus permanen. Lanjutkan?")
                .setPositiveButton("HAPUS") { _, _ ->
                    atletVm.delete(current)
                    Toast.makeText(requireContext(), "Atlet dihapus", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                .setNegativeButton("BATAL", null)
                .show()
        }
    }
}
