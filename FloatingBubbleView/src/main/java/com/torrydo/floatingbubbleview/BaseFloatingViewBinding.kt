package com.torrydo.floatingbubbleview

import android.content.Context
import android.view.View
import androidx.annotation.Discouraged
import androidx.viewbinding.ViewBinding

internal open class BaseFloatingViewBinding<T : ViewBinding>(
    context: Context,
    initializer: T
) : BaseFloatingView(context) {

    private var _binding: T? = null
    val binding get() = _binding!!

    init {
        _binding = initializer
    }

    /**
     * must be root view
     * */
    open fun show() = logIfError {
        super.show(binding.root)
    }

    /**
     * must be root view
     * */
    open fun remove() = logIfError {
        super.remove(binding.root)
    }

    override fun destroy() {
        _binding = null
        super.destroy()
    }

    @Discouraged(message = "use `update()` with no parameter instead")
    override fun update(view: View) {
        super.update(view)
    }

    open fun update() {
        if(binding.root.windowToken == null) return
        super.update(binding.root)
    }

}