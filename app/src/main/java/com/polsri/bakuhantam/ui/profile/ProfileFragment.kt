package com.polsri.bakuhantam.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import com.polsri.bakuhantam.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private enum class Mode { LOGIN, REGISTER }
    private var currentMode = Mode.LOGIN

    // Profile Views
    private lateinit var groupProfileView: Group
    private lateinit var btnLogout: View
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView

    // Login/Register Views
    private lateinit var groupLoginView: Group
    private lateinit var tvLoginTitle: TextView
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
    }

    override fun onResume() {
        super.onResume()
        updateUi()
    }

    private fun findViews(view: View) {
        // Profile
        groupProfileView = view.findViewById(R.id.group_profile_view)
        btnLogout = view.findViewById(R.id.btnLogout)
        tvProfileName = view.findViewById(R.id.tvProfileName)
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail)

        // Login/Register
        groupLoginView = view.findViewById(R.id.group_login_view)
        tvLoginTitle = view.findViewById(R.id.tvLoginTitle)
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword)
        btnMainAction = view.findViewById(R.id.btnMainAction)
        tvSwitchMode = view.findViewById(R.id.tvSwitchMode)
    }

    private fun setupClickListeners() {
        btnLogout.setOnClickListener {
            val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()
            updateUi()
        }

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

    private fun updateUi() {
        val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val isLogin = prefs.getBoolean("isLogin", false)

        if (isLogin) {
            groupProfileView.visibility = View.VISIBLE
            groupLoginView.visibility = View.GONE
            tvProfileName.text = prefs.getString("nama", "Nama Pengguna")
            tvProfileEmail.text = prefs.getString("email", "email@example.com")
        } else {
            groupProfileView.visibility = View.GONE
            groupLoginView.visibility = View.VISIBLE
            currentMode = Mode.LOGIN
            updateUiForMode()
        }
    }

    private fun updateUiForMode() {
        if (currentMode == Mode.LOGIN) {
            tvLoginTitle.text = "Login to BakuHantam"
            etName.visibility = View.GONE
            etConfirmPassword.visibility = View.GONE
            btnMainAction.text = "Login"
            tvSwitchMode.text = "Belum punya akun? Registrasi di sini"
        } else {
            tvLoginTitle.text = "Registrasi Akun Baru"
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

        if (email.isEmpty() || pass.isEmpty()){
             Toast.makeText(requireContext(), "Email dan Password harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val savedEmail = prefs.getString("email", null)
        val savedPass = prefs.getString("password", null)

        if (email == savedEmail && pass == savedPass) {
            prefs.edit().putBoolean("isLogin", true).commit()
            updateUi()
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
        prefsEditor.putBoolean("isLogin", true)
        prefsEditor.commit()

        Toast.makeText(requireContext(), "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
        updateUi()
    }
}
