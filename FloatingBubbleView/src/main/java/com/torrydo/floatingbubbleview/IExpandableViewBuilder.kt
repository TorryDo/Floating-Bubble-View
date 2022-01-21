package com.torrydo.floatingbubbleview

import android.content.Context
import android.view.View

interface IExpandableViewBuilder {

    fun with(context: Context): IExpandableViewBuilder

    fun setExpandableView(view: View): IExpandableViewBuilder

    fun addExpandableViewListener(event: ExpandableViewEvent): IExpandableViewBuilder

    fun setDimAmount(dimAmount: Float): IExpandableViewBuilder

    fun build(): ExpandableView

}