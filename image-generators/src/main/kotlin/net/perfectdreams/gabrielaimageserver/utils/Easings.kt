package net.perfectdreams.gabrielaimageserver.utils

import kotlin.math.cos
import kotlin.math.sin

object Easings {
    fun easeInOutSine(x: Double): Double {
        return -(cos(Math.PI * x) - 1) / 2;
    }

    fun easeInSine(x: Double): Double {
        return 1 - cos((x * Math.PI) / 2);
    }

    fun easeOutSine(x: Double): Double {
        return sin((x * Math.PI) / 2);
    }

    fun easeLinear(start: Double, end: Double, percent: Double): Double {
        return start+(end-start)*percent;
    }
}