package com.torrydo.floatingbubbleview.viewx

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.torrydo.floatingbubbleview.toPx

object ViewHelper {

    @JvmStatic
    fun fromBitmap(context: Context, bm: Bitmap): View {
        return ImageView(context).apply {
            setImageBitmap(bm)
        }
    }

    @JvmStatic
    fun fromDrawable(context: Context, @DrawableRes drawable: Int): View {
        return ImageView(context).apply {
            setImageDrawable(ContextCompat.getDrawable(context, drawable))
        }
    }

    @JvmOverloads
    @JvmStatic
    fun fromDrawable(
        context: Context,
        @DrawableRes drawable: Int,
        widthDp: Int = 160,
        heightDp: Int = 160,
    ): View {

        val view = fromDrawable(context, drawable)
        return view.apply {
            layoutParams = ViewGroup.LayoutParams(widthDp.toPx(), heightDp.toPx())
        }

    }

}