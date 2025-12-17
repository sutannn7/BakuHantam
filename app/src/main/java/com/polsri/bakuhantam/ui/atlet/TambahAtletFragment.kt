package com.polsri.bakuhantam.ui.atlet

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.viewmodel.AtletViewModel
import kotlinx.coroutines.launch

class TambahAtletFragment : Fragment(R.layout.fragment_tambah_atlet) {

    private val atletVm: AtletViewModel by activityViewModels()

    private var selectedFotoUri: Uri? = null
    private var editAtletId: Int = -1
    private var atletLama: Atlet? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedFotoUri = uri
                view?.findViewById<ImageView>(R.id.imgFoto)?.setImageURI(uri)
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) pickImageLauncher.launch("image/*")
            else Toast.makeText(requireContext(), "Izin galeri ditolak", Toast.LENGTH_SHORT).show()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnPickFoto = view.findViewById<Button>(R.id.btnPickFoto)
        val btnSimpan = view.findViewById<Button>(R.id.btnSimpan)

        val imgFoto = view.findViewById<ImageView>(R.id.imgFoto)
        val etNama = view.findViewById<EditText>(R.id.etNama)
        val etNIM = view.findViewById<EditText>(R.id.etNIM)
        val etProdi = view.findViewById<EditText>(R.id.etProdi)
        val etTahun = view.findViewById<EditText>(R.id.etTahun)
        val etBerat = view.findViewById<EditText>(R.id.etBerat)
        val etTinggi = view.findViewById<EditText>(R.id.etTinggi)
        val etPengalaman = view.findViewById<EditText>(R.id.etPengalaman)
        val etCedera = view.findViewById<EditText>(R.id.etCedera)

        // ====== MODE EDIT (kalau ada atletId) ======
        editAtletId = arguments?.getInt("atletId", -1) ?: -1
        if (editAtletId != -1) {
            btnSimpan.text = "UPDATE ATLET"
            viewLifecycleOwner.lifecycleScope.launch {
                val a = atletVm.getById(editAtletId)
                if (a == null) {
                    Toast.makeText(requireContext(), "Atlet tidak ditemukan", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    return@launch
                }
                atletLama = a

                etNama.setText(a.nama)
                etNIM.setText(a.nim)
                etProdi.setText(a.prodi)
                etTahun.setText(a.tahunMasuk.toString())
                etBerat.setText(a.berat.toString())
                etTinggi.setText(a.tinggi.toString())
                etPengalaman.setText(a.pengalaman.toString())
                etCedera.setText(a.riwayatCedera ?: "")

                if (!a.fotoUri.isNullOrBlank()) {
                    selectedFotoUri = Uri.parse(a.fotoUri)
                    imgFoto.setImageURI(selectedFotoUri)
                }
            }
        }

        btnPickFoto.setOnClickListener { openGalleryWithPermissionIfNeeded() }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nim = etNIM.text.toString().trim()
            val prodi = etProdi.text.toString().trim()

            val tahunMasuk = etTahun.text.toString().trim().toIntOrNull()
            val berat = etBerat.text.toString().trim().toFloatOrNull()
            val tinggiCm = etTinggi.text.toString().trim().toFloatOrNull()

            val pengalaman = etPengalaman.text.toString().trim().toIntOrNull() ?: 0
            val cedera = etCedera.text.toString().trim().ifBlank { null }

            if (nama.isEmpty() || nim.isEmpty() || prodi.isEmpty() || tahunMasuk == null || berat == null || tinggiCm == null) {
                Toast.makeText(requireContext(), "Lengkapi data wajib!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // BMI
            val tinggiM = tinggiCm / 100f
            val bmi = if (tinggiM > 0f) berat / (tinggiM * tinggiM) else 0f

            // kelas auto
            val kelas = hitungKelas(berat)

            if (editAtletId == -1) {
                // ====== INSERT ======
                val atletBaru = Atlet(
                    nama = nama,
                    nim = nim,
                    prodi = prodi,
                    tahunMasuk = tahunMasuk,
                    fotoUri = selectedFotoUri?.toString(),
                    berat = berat,
                    tinggi = tinggiCm,
                    kelas = kelas,
                    bmi = bmi,
                    pengalaman = pengalaman,
                    riwayatCedera = cedera
                )
                atletVm.insert(atletBaru)
                Toast.makeText(requireContext(), "Atlet ditambahkan!", Toast.LENGTH_SHORT).show()
            } else {
                // ====== UPDATE ======
                val lama = atletLama ?: run {
                    Toast.makeText(requireContext(), "Data lama belum siap", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val atletUpdate = lama.copy(
                    nama = nama,
                    nim = nim,
                    prodi = prodi,
                    tahunMasuk = tahunMasuk,
                    fotoUri = selectedFotoUri?.toString(),
                    berat = berat,
                    tinggi = tinggiCm,
                    kelas = kelas,
                    bmi = bmi,
                    pengalaman = pengalaman,
                    riwayatCedera = cedera
                )

                atletVm.update(atletUpdate)
                Toast.makeText(requireContext(), "Atlet diupdate!", Toast.LENGTH_SHORT).show()
            }

            findNavController().popBackStack()
        }
    }

    private fun hitungKelas(berat: Float): String {
        return when {
            berat <= 52 -> "Flyweight"
            berat <= 56 -> "Bantamweight"
            berat <= 60 -> "Featherweight"
            berat <= 64 -> "Lightweight"
            berat <= 69 -> "Welterweight"
            berat <= 75 -> "Middleweight"
            berat <= 81 -> "Light Heavyweight"
            else -> "Heavyweight"
        }
    }

    private fun openGalleryWithPermissionIfNeeded() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> pickImageLauncher.launch("image/*")
        }
    }
}
