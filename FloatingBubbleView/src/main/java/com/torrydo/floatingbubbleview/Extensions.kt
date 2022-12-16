package com.torrydo.floatingbubbleview

import android.content.Context
import android.content.res.Resources.getSystem
import android.graphics.Bitmap
import android.graphics.Point
import android.util.Size
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.lang.ref.WeakReference

internal fun Int.toBitmap(context: Context): Bitmap? {
    return WeakReference(context).get()?.let { weakContext ->
        ContextCompat.getDrawable(weakContext, this)!!.toBitmap()
    }
}

/**
 * @return Point( -x .. x, -y .. y )
 * */
internal fun View.getXYPointOnScreen(): Point {
    val arr = IntArray(2)
    this.getLocationOnScreen(arr)

    return Point(arr[0], arr[1])
}

internal val Int.toDp: Int get() = (this / getSystem().displayMetrics.density).toInt()
internal val Int.toPx: Int get() = (this * getSystem().displayMetrics.density).toInt()

inline fun View.afterMeasured(crossinline afterMeasuredWork: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                afterMeasuredWork()
            }
        }
    })
}

internal fun Size.isZero() = width == 0 && height == 0
internal fun Size.notZero() = width != 0 && height != 0
internal fun Size.largerThanZero() = width > 0 && height > 0
