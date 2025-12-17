package com.polsri.bakuhantam.utils

object BMIUtils {
    fun hitung(berat: Float, tinggiCm: Float): Float {
        val tinggi = tinggiCm / 100f
        if (tinggi <= 0f) return 0f
        return berat / (tinggi * tinggi)
    }
}
