package com.polsri.bakuhantam.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R

class LoginFragment : Fragment(R.layout.fragment_login) {

    private enum class Mode { LOGIN, REGISTER }
    private var currentMode = Mode.LOGIN

    private lateinit var tvTitle: TextView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnMainAction: Button
    private lateinit var tvSwitchMode: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
        setupClickListeners()
        updateUiForMode()
    }

    private fun findViews(view: View) {
        tvTitle = view.findViewById(R.id.tvTitle)
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        btnMainAction = view.findViewById(R.id.btnMainAction)
        tvSwitchMode = view.findViewById(R.id.tvSwitchMode)
    }

    private fun setupClickListeners() {
        btnMainAction.setOnClickListener {
            if (currentMode == Mode.LOGIN) {
                handleLogin()
            } else {
                handleRegistration()
            }
        }

        tvSwitchMode.setOnClickListener {
            currentMode = if (currentMode == Mode.LOGIN) Mode.REGISTER else Mode.LOGIN
            updateUiForMode()
        }
    }

    private fun updateUiForMode() {
        if (currentMode == Mode.LOGIN) {
            tvTitle.text = "Login to BakuHantam"
            etName.visibility = View.GONE
            etConfirmPassword.visibility = View.GONE
            btnMainAction.text = "Login"
            tvSwitchMode.text = "Belum punya akun? Registrasi di sini"
        } else {
            tvTitle.text = "Registrasi Akun Baru"
            etName.visibility = View.VISIBLE
            etConfirmPassword.visibility = View.VISIBLE
            btnMainAction.text = "Registrasi"
            tvSwitchMode.text = "Sudah punya akun? Login di sini"
        }
    }

    private fun handleLogin() {
        val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val email = etEmail.text.toString().trim()
        val pass = etPassword.text.toString().trim()

        val savedEmail = prefs.getString("email", null)
        val savedPass = prefs.getString("password", null)

        if (email.isNotEmpty() && pass.isNotEmpty() && email == savedEmail && pass == savedPass) {
            prefs.edit().putBoolean("isLogin", true).commit() // Using commit for synchronous save
            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
        } else {
            Toast.makeText(requireContext(), "Email atau Password salah", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleRegistration() {
        val prefsEditor = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE).edit()
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val pass = etPassword.text.toString().trim()
        val confirmPass = etConfirmPassword.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(requireContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPass) {
            Toast.makeText(requireContext(), "Password tidak cocok", Toast.LENGTH_SHORT).show()
            return
        }

        prefsEditor.putString("nama", name)
        prefsEditor.putString("email", email)
        prefsEditor.putString("password", pass)
        prefsEditor.putBoolean("isLogin", true) // Langsung login setelah registrasi
        prefsEditor.commit() // Using commit for synchronous save

        Toast.makeText(requireContext(), "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
    }
}
