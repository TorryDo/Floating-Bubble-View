package com.torrydo.floatingbubbleview

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewTreeObserver


// exclude view gesture on home screen -------------------------------------------------------------
private var exclusionRects: MutableList<Rect> = ArrayList()

internal fun View.updateGestureExclusion() {
    if (Build.VERSION.SDK_INT < AndroidVersions.`10`) return

    val screenSize = sez.fullSize

    exclusionRects.clear()

    val rect = Rect(0, 0, this.width, screenSize.height)
    exclusionRects.add(rect)


    this.systemGestureExclusionRects = exclusionRects
}

// permission --------------------------------------------------------------------------------------

/**
 * by default, display over other app permission will be granted automatically if android's version smaller than android M
 *
 * - some MIUI devices may not work properly
 *
 * */
internal fun Context.canDrawOverlays(): Boolean {

    if (Build.VERSION.SDK_INT < AndroidVersions.`6`) {
        return true
    }

    return Settings.canDrawOverlays(this)
}

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

/**
 * @return Point( 0 .. x, 0 .. y )
 * */
internal fun View.getXYOnScreen(): Pair<Int, Int> {
    val arr = IntArray(2)
    this.getLocationOnScreen(arr)

    return Pair(arr[0], arr[1])
}
