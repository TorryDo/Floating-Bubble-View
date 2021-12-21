package com.torrydo.floatingbubbleview.main.layout_view

import android.content.Context
import android.view.View

interface IExpandableViewBuilder {

    fun with(context: Context): IExpandableViewBuilder

    fun setExpandableView(view: View): IExpandableViewBuilder

    fun addExpandableViewListener(listener: ExpandableViewListener): IExpandableViewBuilder

    fun setDimAmount(dimAmount: Float): IExpandableViewBuilder

    fun build(): ExpandableView

}