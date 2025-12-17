package com.polsri.bakuhantam.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNama = view.findViewById<EditText>(R.id.etNama)
        val etUsername = view.findViewById<EditText>(R.id.etUsername)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val ivGoogle = view.findViewById<ImageView>(R.id.ivGoogle)
        val ivFacebook = view.findViewById<ImageView>(R.id.ivFacebook)

        val prefs = requireContext().getSharedPreferences("auth", 0)

        btnRegister.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (nama.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Lengkapi data dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit()
                .putString("nama", nama)
                .putString("username", username)
                .putString("email", email)
                .putString("password", pass)
                .putBoolean("isLogin", true)
                .apply()

            findNavController().navigate(R.id.action_loginFragment_to_profileFragment)
        }

        ivGoogle.setOnClickListener {
            // TODO: Implement Google Sign-In
            Toast.makeText(requireContext(), "Google Sign-In coming soon!", Toast.LENGTH_SHORT).show()
        }

        ivFacebook.setOnClickListener {
            // TODO: Implement Facebook Login
            Toast.makeText(requireContext(), "Facebook Login coming soon!", Toast.LENGTH_SHORT).show()
        }
    }
}
