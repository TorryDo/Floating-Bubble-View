package com.torrydo.floatingbubbleview.main.layout_view

import android.content.Context
import android.view.View

class ExpandableViewBuilder : IExpandableViewBuilder {

    lateinit var context: Context

    var rootView: View? = null
    var listener = object : ExpandableViewListener {}

    var dim = 0.5f

    override fun with(context: Context): ExpandableViewBuilder {
        this.context = context
        return this
    }

    override fun setExpandableView(view: View): ExpandableViewBuilder {
        this.rootView = view
        return this
    }

    override fun setExpandableViewListener(listener: ExpandableViewListener): ExpandableViewBuilder {
        this.listener = listener
        return this
    }

    override fun setDimAmount(dimAmount: Float): ExpandableViewBuilder {
        this.dim = dimAmount
        return this
    }

    override fun build(): ExpandableView {
        return ExpandableView(this)
    }

}