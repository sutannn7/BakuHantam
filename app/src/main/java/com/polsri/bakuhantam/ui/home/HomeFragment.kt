package com.polsri.bakuhantam.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.polsri.bakuhantam.R
import com.polsri.bakuhantam.databinding.FragmentHomeBinding
import com.polsri.bakuhantam.viewmodel.HomeViewModel

class HomeFragment : Fragment() { // Removed the layout ID from the constructor

    private val viewModel: HomeViewModel by viewModels()

    // Declare the binding variable
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using view binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // === Setup Navigation Clicks using the binding object ===
        binding.btnTambahAtlet.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_tambahAtletFragment) }
        binding.btnBuatPertandingan.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_buatPertandinganFragment) }
        binding.qaHistory.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_historyFragment) }
        binding.qaLeaderboard.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_leaderboardFragment) }
        binding.qaAtlet.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_atletListFragment) }

        // === Observe Data from ViewModel using the binding object ===
        viewModel.run {
            totalAtlet.observe(viewLifecycleOwner, Observer <Int> { total ->
                binding.tvStatAtlet.text = "Total Atlet: $total"
            })

            totalPertandingan.observe(viewLifecycleOwner, Observer <Int> { total ->
                binding.tvStatTotalMatch.text = "Total Match: $total"
            })

            totalPertandinganHariIni.observe(viewLifecycleOwner, Observer <Int>  { total ->
                binding.tvStatMatchToday.text = "Match Hari Ini: $total"
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding object to avoid memory leaks
        _binding = null
    }
}
