package com.torrydo.floatingbubbleview.helper

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.torrydo.floatingbubbleview.toPx


object ViewHelper {

    //region load bubble
    @JvmStatic
    fun fromBitmap(context: Context, bm: Bitmap): View {
        return ImageView(context).apply {
            setImageBitmap(bm)
        }
    }

    @JvmStatic
    fun fromBitmap(context: Context, bm: Bitmap, widthDp: Int, heightDp: Int): View {
        val view = fromBitmap(context, bm)
        return view.apply {
            layoutParams = ViewGroup.LayoutParams(widthDp.toPx(), heightDp.toPx())
        }
    }

    @JvmStatic
    fun fromDrawable(context: Context, @DrawableRes drawable: Int): View {
        return ImageView(context).apply {
            setImageDrawable(ContextCompat.getDrawable(context, drawable))
        }
    }

    @JvmStatic
    fun fromDrawable(
        context: Context,
        @DrawableRes drawable: Int,
        widthDp: Int,
        heightDp: Int,
    ): View {
        val view = fromDrawable(context, drawable)
        return view.apply {
            layoutParams = ViewGroup.LayoutParams(widthDp.toPx(), heightDp.toPx())
        }

    }
    //endregion

}