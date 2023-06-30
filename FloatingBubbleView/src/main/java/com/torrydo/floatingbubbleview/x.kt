package com.torrydo.floatingbubbleview

import android.util.Size

// x means extension. ^^

/**
 * return size of the bubble view if it's not null
 *
 * otherwise the bubble size variable
 * */
fun FloatingBubble.Builder.bubbleSizeCompat(): Size {
    return if (bubbleView != null) {
        Size(bubbleView!!.width, bubbleView!!.height)
    } else {
        bubbleSizePx
    }
}

//fun FloatingBubble.Builder.bubbleWidthCompat(): Int {
//    return if (bubbleView != null) {
//        bubbleView!!.width
//    } else {
//        bubbleSizePx.width
//    }
//}