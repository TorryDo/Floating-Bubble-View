package com.torrydo.floatingbubbleview

import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Bitmap
import android.graphics.Point
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

fun Int.toBitmap(context: Context): Bitmap {
    return ContextCompat.getDrawable(context, this)!!.toBitmap()
}

fun String.toTag() = "<> $this"

// -x | x
fun View.getXYPointOnScreen(): Point {
    val arr = IntArray(2)
    this.getLocationOnScreen(arr)

    return Point(arr[0], arr[1])
}

val Int.toDp: Int get() = (this / getSystem().displayMetrics.density).toInt()
val Int.toPx: Int get() = (this * getSystem().displayMetrics.density).toInt()