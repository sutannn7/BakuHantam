package com.polsri.bakuhantam.utils

object KelasUtils {
    fun hitungKelas(berat: Float): String =
        when {
            berat < 52f -> "Flyweight"
            berat < 60f -> "Lightweight"
            berat < 70f -> "Middleweight"
            else -> "Heavyweight"
        }
}
