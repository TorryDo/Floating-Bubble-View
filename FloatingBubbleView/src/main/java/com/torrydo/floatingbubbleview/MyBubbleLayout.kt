package com.torrydo.floatingbubbleview

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.LinearLayout

internal open class MyBubbleLayout(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(
    context,
    attrs,
) {

    internal var ignoreChildEvent: (MotionEvent) -> Boolean = { false }
    internal var doOnTouchEvent: (MotionEvent) -> Unit = {}

    private var onDispatchKeyEvent: ((KeyEvent) -> Boolean?)? = null

    fun setOnDispatchKeyEvent(callback: ((KeyEvent) -> Boolean?)){
        onDispatchKeyEvent = callback
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (onDispatchKeyEvent != null && event != null) {
            return onDispatchKeyEvent!!(event) ?: super.dispatchKeyEvent(event)
        }
        return super.dispatchKeyEvent(event)
    }

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