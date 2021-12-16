package com.torrydo.floatingview.bubble

import android.content.Context
import android.graphics.Bitmap
import com.torrydo.floatingview.Extension.toBitmap
import com.torrydo.floatingview.R

class FloatingBubble(
    var context: Context,
    var iconBitmap: Bitmap,
    var iconXBitmap: Bitmap,
    var bubbleSize: Int,
    var movable: Boolean,
    var elevation: Int,
    var alpha: Float
) {

    private fun setup(){

    }

    fun show(){}

    fun hide(){}

    fun remove(){}

}