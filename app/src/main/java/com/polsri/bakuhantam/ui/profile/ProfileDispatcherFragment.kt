package com.polsri.bakuhantam.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R

class ProfileDispatcherFragment : Fragment(R.layout.fragment_profile_dispatcher) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
        val isLogin = prefs.getBoolean("isLogin", false)

        if (isLogin) {
            findNavController().navigate(R.id.action_profileDispatcherFragment_to_profileFragment)
        } else {
            findNavController().navigate(R.id.action_profileDispatcherFragment_to_loginFragment)
        }
    }
}
