package com.torrydo.floatingbubbleview.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.view.View

object Extension {

    fun Int.toBitmap(): Bitmap {
        return BitmapFactory.decodeResource(Resources.getSystem(), this)
    }

}

fun String.toTag() = "<> $this"

fun View.getXYPointOnScreen(): Point {
    val arr = IntArray(2)
    this.getLocationOnScreen(arr)

    return Point(arr[0], arr[1])
}