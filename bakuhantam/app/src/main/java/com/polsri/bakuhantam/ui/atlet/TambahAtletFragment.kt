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
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.data.database.entity.Atlet
import com.polsri.bakuhantam.viewmodel.AtletViewModel

class TambahAtletFragment : Fragment(R.layout.fragment_tambah_atlet) {

    private val atletVm: AtletViewModel by activityViewModels()

    private var selectedFotoUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedFotoUri = uri
                view?.findViewById<ImageView>(R.id.imgFoto)?.setImageURI(uri)
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                pickImageLauncher.launch("image/*")
            } else {
                Toast.makeText(requireContext(), "Izin galeri ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnPickFoto = view.findViewById<Button>(R.id.btnPickFoto)
        val btnSimpan = view.findViewById<Button>(R.id.btnSimpan)

        val etNama = view.findViewById<EditText>(R.id.etNama)
        val etNIM = view.findViewById<EditText>(R.id.etNIM)
        val etProdi = view.findViewById<EditText>(R.id.etProdi)
        val etTahun = view.findViewById<EditText>(R.id.etTahun)
        val etBerat = view.findViewById<EditText>(R.id.etBerat)
        val etTinggi = view.findViewById<EditText>(R.id.etTinggi)
        val etPengalaman = view.findViewById<EditText>(R.id.etPengalaman)
        val etCedera = view.findViewById<EditText>(R.id.etCedera)

        btnPickFoto.setOnClickListener {
            openGalleryWithPermissionIfNeeded()
        }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val nim = etNIM.text.toString().trim()
            val prodi = etProdi.text.toString().trim()
            val tahunStr = etTahun.text.toString().trim()
            val beratStr = etBerat.text.toString().trim()
            val tinggiStr = etTinggi.text.toString().trim()
            val pengalamanStr = etPengalaman.text.toString().trim()
            val cederaText = etCedera.text.toString().trim()

            if (nama.isEmpty() || nim.isEmpty() || prodi.isEmpty() ||
                tahunStr.isEmpty() || beratStr.isEmpty() ||
                tinggiStr.isEmpty() || pengalamanStr.isEmpty()
            ) {
                Toast.makeText(requireContext(), "Lengkapi data dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val berat = beratStr.toFloat()
            val tinggiCm = tinggiStr.toFloat()

            // ✅ BMI (wajib di entity)
            val tinggiM = tinggiCm / 100f
            val bmi = if (tinggiM > 0f) berat / (tinggiM * tinggiM) else 0f

            // ✅ Kelas (wajib di entity)
            val kelas = hitungKelas(berat)

            val atlet = Atlet(
                nama = nama,
                nim = nim,
                prodi = prodi,
                tahunMasuk = tahunStr.toInt(),
                fotoUri = selectedFotoUri?.toString(),

                berat = berat,
                tinggi = tinggiCm,
                kelas = kelas,
                bmi = bmi,

                pengalaman = pengalamanStr.toInt(),

                // ✅ ini yang benar, bukan "Cedera"
                riwayatCedera = cederaText.ifBlank { null }
            )

            atletVm.insert(atlet)
            Toast.makeText(requireContext(), "Atlet tersimpan", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun hitungKelas(berat: Float): String {
        return when {
            berat < 57f -> "Flyweight"
            berat < 70f -> "Lightweight"
            berat < 84f -> "Middleweight"
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
            else -> {
                pickImageLauncher.launch("image/*")
            }
        }
    }
}
