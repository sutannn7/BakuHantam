package com.polsri.bakuhantam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val nav = findNavController(R.id.nav_host_fragment)
        val bottom = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottom.setupWithNavController(nav)
    }
}
