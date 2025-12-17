package com.polsri.bakuhantam.ui.pertandingan

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

    // kelas yang dipilih manual dari dropdown
    private var selectedKelas: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnA = view.findViewById<Button>(R.id.btnPilihA)
        val btnB = view.findViewById<Button>(R.id.btnPilihB)
        val btnWasit = view.findViewById<Button>(R.id.btnPilihWasit)
        val btnMulai = view.findViewById<Button>(R.id.btnMulai)

        // ================= KELAS =================
        val kelasBox = view.findViewById<View>(R.id.kelasBox)
        val tvKelas = view.findViewById<TextView>(R.id.tvKelas)

        val listKelas = arrayOf(
            "Flyweight",
            "Lightweight",
            "Middleweight",
            "Heavyweight"
        )

        kelasBox.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Pilih Kelas")
                .setItems(listKelas) { _, which ->
                    selectedKelas = listKelas[which]
                    tvKelas.text = selectedKelas
                }
                .show()
        }

        // ================= ATLET A/B =================
        val atletABox = view.findViewById<View>(R.id.atletABox)
        val atletBBox = view.findViewById<View>(R.id.atletBBox)
        val tvAtletA = view.findViewById<TextView>(R.id.tvAtletA)
        val tvAtletB = view.findViewById<TextView>(R.id.tvAtletB)

        fun showAtletPicker(title: String, onPicked: (Atlet) -> Unit) {
            val list = atletVm.atletList.value
            if (list.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Belum ada atlet", Toast.LENGTH_SHORT).show()
                return
            }

            val names = list.map { it.nama }.toTypedArray()

            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setItems(names) { _, which ->
                    onPicked(list[which])
                }
                .show()
        }

        atletABox.setOnClickListener {
            showAtletPicker("Pilih Atlet A") { picked ->
                if (atletB?.id == picked.id) {
                    Toast.makeText(
                        requireContext(),
                        "Atlet ini sudah dipilih di B",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@showAtletPicker
                }
                atletA = picked
                tvAtletA.text = picked.nama
                btnA.text = "A: ${picked.nama}"

                // kalau belum pilih kelas manual, ikut kelas atlet A (flow awal)
                if (selectedKelas == null) {
                    tvKelas.text = picked.kelas
                }
            }
        }

        atletBBox.setOnClickListener {
            showAtletPicker("Pilih Atlet B") { picked ->
                if (atletA?.id == picked.id) {
                    Toast.makeText(
                        requireContext(),
                        "Atlet ini sudah dipilih di A",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@showAtletPicker
                }
                atletB = picked
                tvAtletB.text = picked.nama
                btnB.text = "B: ${picked.nama}"
            }
        }

        // tombol lama tetap dipakai -> diarahkan ke dropdown
        btnA.setOnClickListener { atletABox.performClick() }
        btnB.setOnClickListener { atletBBox.performClick() }

        // ================= WASIT =================
        val wasitBox = view.findViewById<View>(R.id.wasitBox)
        val tvWasit = view.findViewById<TextView>(R.id.tvWasit)

        // AUTO-SEED WASIT DEFAULT kalau masih kosong
        viewLifecycleOwner.lifecycleScope.launch {
            val wasitListNow = wasitVm.wasitList.value
            if (wasitListNow.isNullOrEmpty()) {
                val wDefault = Wasit(
                    nama = "Wasit Default",
                    nim = "000",
                    prodi = "-",
                    fotoUri = null
                )
                wasitVm.insert(wDefault)
            }
        }

        fun showWasitPicker(title: String, onPicked: (Wasit) -> Unit) {
            val list = wasitVm.wasitList.value
            if (list.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Belum ada wasit", Toast.LENGTH_SHORT).show()
                return
            }

            val names = list.map { it.nama }.toTypedArray()

            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setItems(names) { _, which ->
                    onPicked(list[which])
                }
                .show()
        }

        wasitBox.setOnClickListener {
            showWasitPicker("Pilih Wasit") { picked ->
                wasit = picked
                tvWasit.text = picked.nama
                btnWasit.text = "Wasit: ${picked.nama}"
            }
        }

        // tombol lama tetap bisa dipakai -> diarahkan ke dropdown
        btnWasit.setOnClickListener { wasitBox.performClick() }

        // ================= MULAI =================
        btnMulai.setOnClickListener {
            if (atletA == null || atletB == null || wasit == null) {
                Toast.makeText(
                    requireContext(),
                    "Pilih Atlet dan Wasit terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val beratDiff = kotlin.math.abs(atletA!!.berat - atletB!!.berat)
            val tinggiDiff = kotlin.math.abs(atletA!!.tinggi - atletB!!.tinggi)

            if (beratDiff > 15f || tinggiDiff > 20f) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Peringatan")
                    .setMessage("Perbedaan berat $beratDiff kg & tinggi $tinggiDiff cm terlalu jauh. Lanjutkan?")
                    .setPositiveButton("Lanjut") { _, _ ->
                        createMatch(atletA!!, atletB!!, wasit!!)
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            } else {
                createMatch(atletA!!, atletB!!, wasit!!)
            }
        }
    }

    private fun createMatch(a: Atlet, b: Atlet, w: Wasit) {
        viewLifecycleOwner.lifecycleScope.launch {
            val kelasFinal = selectedKelas ?: a.kelas

            val p = Pertandingan(
                kelas = kelasFinal,
                idAtletA = a.id,
                idAtletB = b.id,
                idWasit = w.id
            )

            // fungsi di ViewModel mengembalikan Long
            val idLong = pVm.buatPertandinganReturnId(p)
            val idInt = idLong.toInt()

            Toast.makeText(requireContext(), "Pertandingan dibuat", Toast.LENGTH_SHORT).show()

            // kirim id ke MatchRoomFragment via Bundle biasa
            val bundle = Bundle().apply {
                putInt("pertandinganId", idInt)
            }
            findNavController().navigate(R.id.matchRoomFragment, bundle)
        }
    }
}
