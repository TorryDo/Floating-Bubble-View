package com.torrydo.floatingbubbleview

import kotlin.math.pow
import kotlin.math.sqrt

internal object XMath {

    fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
        return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    }

}