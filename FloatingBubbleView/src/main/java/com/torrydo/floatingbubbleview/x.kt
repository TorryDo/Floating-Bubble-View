package com.torrydo.floatingbubbleview

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Point
import android.util.Size
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.lang.ref.WeakReference


/**
 * return size of the bubble view if it's not null
 *
 * otherwise the bubble size variable
 * */
internal fun FloatingBubble.Builder.bubbleSize(): Size {
    return Size(bubbleView!!.width, bubbleView!!.height)
}

internal fun Int.toBitmap(context: Context): Bitmap? {
    return WeakReference(context).get()?.let { weakContext ->
        ContextCompat.getDrawable(weakContext, this)!!.toBitmap()
    }
}



internal fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
internal fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

