package com.torrydo.floatingbubbleview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

internal class MyBubbleLayout(
    context: Context,
    attrs: AttributeSet?,
) : LinearLayout(
    context,
    attrs,
) {

    internal var ignoreChildEvent: (MotionEvent) -> Boolean = { false }
    internal var doOnTouchEvent: (MotionEvent) -> Unit = {}

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        val b = ev?.let{
            doOnTouchEvent(it)
            ignoreChildEvent(it)
        }
        return b ?: onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let(doOnTouchEvent)
        return super.onTouchEvent(event)
    }


}