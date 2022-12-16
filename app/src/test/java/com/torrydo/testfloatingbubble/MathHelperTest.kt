package com.torrydo.testfloatingbubble

import com.torrydo.floatingbubbleview.MathHelper
import org.junit.Assert.*

import org.junit.Test

class MathHelperTest {

    @Test
    fun distance() {

        val rs = MathHelper.distance(-7.0, -4.0, 17.0, 6.5)

        println(rs)
    }
}