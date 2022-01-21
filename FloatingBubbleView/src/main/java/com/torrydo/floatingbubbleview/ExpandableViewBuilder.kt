package com.torrydo.floatingbubbleview

import android.content.Context
import android.view.View

class ExpandableViewBuilder : IExpandableViewBuilder {

    lateinit var context: Context

    var rootView: View? = null
    var listener = object : ExpandableView.Event {}

    var dim = 0.5f

    override fun with(context: Context): ExpandableViewBuilder {
        this.context = context
        return this
    }

    override fun setExpandableView(view: View): ExpandableViewBuilder {
        this.rootView = view
        return this
    }

    override fun addExpandableViewListener(event: ExpandableView.Event): ExpandableViewBuilder {
        this.listener = event
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