package com.torrydo.floatingview.view

import android.view.View

interface IFloatingViewBuilder {

    fun setView(view: View): FloatingViewBuilder

    fun setWidth(dp: Int): FloatingViewBuilder
    fun setHeight(dp: Int): FloatingViewBuilder

    fun setMovable(boolean: Boolean): FloatingViewBuilder

    fun setDim(): FloatingViewBuilder
    fun setAlpha(alpha: Float): FloatingViewBuilder

    fun build(): FloatingView

}