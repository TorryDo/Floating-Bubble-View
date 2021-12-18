package com.torrydo.floatingbubble.main_icon

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.torrydo.floatingbubble.FloatingBubbleBuilder
import com.torrydo.floatingbubble.R
import com.torrydo.floatingbubble.base.BaseFloatingView
import com.torrydo.floatingbubble.databinding.IconMainBinding
import com.torrydo.floatingbubble.utils.Utils

class FloatingBubbleIcon(bubbleBuilder: FloatingBubbleBuilder) : BaseFloatingView(bubbleBuilder.context) {

    private val TAG = Utils.getTagName()

    private var binding = IconMainBinding.inflate(LayoutInflater.from(bubbleBuilder.context))

    init {
        setupDefaultLayoutParams()
        binding.homeLauncherMainIcon.setImageResource(R.drawable.ic_rounded_blue_diamond)
    }

    // must be root view
    fun show(){
        super.show(binding.root)
    }
    fun remove(){
        super.remove(binding.root)
    }

    override fun setupDefaultLayoutParams() {
        super.setupDefaultLayoutParams()
        print("hello from $TAG")
    }
}