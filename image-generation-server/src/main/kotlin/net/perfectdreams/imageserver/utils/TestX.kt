package net.perfectdreams.imageserver.utils

fun main() {
    fun buildFfmpegStuff(start: Int): String {
        val hours = start / 3600
        val minutes = (start % 3600) / 60
        val seconds = start % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    println(buildFfmpegStuff((4267 / 29.97).toInt()))
}