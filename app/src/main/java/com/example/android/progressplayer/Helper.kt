package com.example.android.progressplayer


import java.util.*

fun runTimer(seconds: Int): String {
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format(
        Locale.getDefault(),
        "%02d:%02d", minutes, secs
    )
}



