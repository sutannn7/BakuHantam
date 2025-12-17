package com.polsri.bakuhantam.ui.pertandingan

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.data.database.entity.Pertandingan
import com.polsri.bakuhantam.data.database.entity.Wasit
import com.polsri.bakuhantam.viewmodel.AtletViewModel
import com.polsri.bakuhantam.viewmodel.PertandinganViewModel
import com.polsri.bakuhantam.viewmodel.WasitViewModel
import kotlinx.coroutines.launch

class BuatPertandinganFragment : Fragment(R.layout.fragment_buat_pertandingan) {

    private val atletVm: AtletViewModel by activityViewModels()
    private val pVm: PertandinganViewModel by activityViewModels()
    private val wasitVm: WasitViewModel by activityViewModels()

    private var atletA: Atlet? = null
    private var atletB: Atlet? = null
    private var wasit: Wasit? = null

    private var selectedKelas: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnA = view.findViewById<Button>(R.id.btnPilihA)
        val btnB = view.findViewById<Button>(R.id.btnPilihB)
        val btnWasit = view.findViewById<Button>(R.id.btnPilihWasit)
        val btnMulai = view.findViewById<Button>(R.id.btnMulai)

        val kelasBox = view.findViewById<View>(R.id.kelasBox)
        val tvKelas = view.findViewById<TextView>(R.id.tvKelas)

        val listKelas = arrayOf("Flyweight", "Lightweight", "Middleweight", "Heavyweight")

        kelasBox.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Pilih Kelas")
                .setItems(listKelas) { _, which ->
                    selectedKelas = listKelas[which]
                    tvKelas.text = selectedKelas
                }
                .show()
        }

        val atletABox = view.findViewById<View>(R.id.atletABox)
        val atletBBox = view.findViewById<View>(R.id.atletBBox)
        val tvAtletA = view.findViewById<TextView>(R.id.tvAtletA)
        val tvAtletB = view.findViewById<TextView>(R.id.tvAtletB)

        val wasitBox = view.findViewById<View>(R.id.wasitBox)
        val tvWasit = view.findViewById<TextView>(R.id.tvWasit)

        atletVm.atletList.observe(viewLifecycleOwner, Observer { atletList ->
            if (atletList.isNullOrEmpty()) {
                atletABox.setOnClickListener { Toast.makeText(requireContext(), "Belum ada atlet, silakan tambahkan dulu", Toast.LENGTH_SHORT).show() }
                atletBBox.setOnClickListener { Toast.makeText(requireContext(), "Belum ada atlet, silakan tambahkan dulu", Toast.LENGTH_SHORT).show() }
            } else {
                fun showAtletPicker(title: String, onPicked: (Atlet) -> Unit) {
                    val names = atletList.map { it.nama }.toTypedArray()
                    AlertDialog.Builder(requireContext())
                        .setTitle(title)
                        .setItems(names) { _, which -> onPicked(atletList[which]) }
                        .show()
                }

                atletABox.setOnClickListener { 
                    showAtletPicker("Pilih Atlet A") { picked ->
                         if (atletB?.id == picked.id) {
                            Toast.makeText(requireContext(), "Atlet ini sudah dipilih di B", Toast.LENGTH_SHORT).show()
                            return@showAtletPicker
                        }
                        atletA = picked
                        tvAtletA.text = picked.nama
                        btnA.text = getString(R.string.atlet_a_button, picked.nama)
                        if (selectedKelas == null) tvKelas.text = picked.kelas
                    }
                }

                atletBBox.setOnClickListener {
                    showAtletPicker("Pilih Atlet B") { picked ->
                        if (atletA?.id == picked.id) {
                            Toast.makeText(requireContext(), "Atlet ini sudah dipilih di A", Toast.LENGTH_SHORT).show()
                            return@showAtletPicker
                        }
                        atletB = picked
                        tvAtletB.text = picked.nama
                        btnB.text = getString(R.string.atlet_b_button, picked.nama)
                    }
                }
            }
        })

        wasitVm.wasitList.observe(viewLifecycleOwner, Observer { wasitList ->
            if (wasitList.isNullOrEmpty()) {
                 wasitBox.setOnClickListener { Toast.makeText(requireContext(), "Belum ada wasit", Toast.LENGTH_SHORT).show() }
                 val wDefault = Wasit(nama = "Wasit Default", nim = "000", prodi = "-", fotoUri = null)
                 wasitVm.insert(wDefault)
            } else {
                fun showWasitPicker(title: String, onPicked: (Wasit) -> Unit) {
                    val names = wasitList.map { it.nama }.toTypedArray()
                    AlertDialog.Builder(requireContext())
                        .setTitle(title)
                        .setItems(names) { _, which -> onPicked(wasitList[which]) }
                        .show()
                }

                wasitBox.setOnClickListener {
                    showWasitPicker("Pilih Wasit") { picked ->
                        wasit = picked
                        tvWasit.text = picked.nama
                        btnWasit.text = getString(R.string.wasit_button, picked.nama)
                    }
                }
            }
        })

        btnA.setOnClickListener { atletABox.performClick() }
        btnB.setOnClickListener { atletBBox.performClick() }
        btnWasit.setOnClickListener { wasitBox.performClick() }

        btnMulai.setOnClickListener {
            if (atletA == null || atletB == null || wasit == null) {
                Toast.makeText(requireContext(), "Pilih Atlet dan Wasit terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createMatch(atletA!!, atletB!!, wasit!!)
        }
    }

    private fun createMatch(a: Atlet, b: Atlet, w: Wasit) {
        val beratDiff = kotlin.math.abs(a.berat - b.berat)
        val tinggiDiff = kotlin.math.abs(a.tinggi - b.tinggi)

        if (beratDiff > 15f || tinggiDiff > 20f) {
            AlertDialog.Builder(requireContext())
                .setTitle("Peringatan")
                .setMessage("Perbedaan berat $beratDiff kg & tinggi $tinggiDiff cm terlalu jauh. Lanjutkan?")
                .setPositiveButton("Lanjut") { _, _ -> proceedCreateMatch(a, b, w) }
                .setNegativeButton("Batal", null)
                .show()
        } else {
            proceedCreateMatch(a, b, w)
        }
    }

    private fun proceedCreateMatch(a: Atlet, b: Atlet, w: Wasit) {
        viewLifecycleOwner.lifecycleScope.launch {
            val kelasFinal = selectedKelas ?: a.kelas
            val p = Pertandingan(
                kelas = kelasFinal,
                idAtletA = a.id,
                idAtletB = b.id,
                idWasit = w.id,
                tanggal = System.currentTimeMillis()
            )
            val idLong = pVm.buatPertandinganReturnId(p)
            Toast.makeText(requireContext(), "Pertandingan dibuat", Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply { putInt("pertandinganId", idLong.toInt()) }
            findNavController().navigate(R.id.matchRoomFragment, bundle)
        }
    }
}
