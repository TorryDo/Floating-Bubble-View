package com.torrydo.floatingbubbleview

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.util.Size
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.torrydo.screenez.ScreenEasy
import java.lang.ref.WeakReference



internal fun Int.toBitmap(context: Context): Bitmap? {
    return WeakReference(context).get()?.let { weakContext ->
        ContextCompat.getDrawable(weakContext, this)!!.toBitmap()
    }
}

internal fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
internal fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

internal val sez = ScreenEasy()


