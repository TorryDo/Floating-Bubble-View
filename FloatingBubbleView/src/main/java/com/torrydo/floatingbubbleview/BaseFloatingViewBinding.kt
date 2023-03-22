package com.torrydo.floatingbubbleview

import android.content.Context
import android.util.Log
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
    open fun show() {
        try {
            if(binding.root.windowToken != null) return
            super.show(binding.root)
        }catch (e: Exception){
//            Log.d("<>", "show: ${e.stackTraceToString()}"); this line show error in some cases
        }
    }

    /**
     * must be root view
     * */
    open fun remove() {
        try {
            super.remove(binding.root)
        }catch (e: Exception){
            e.printStackTrace()
        }
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