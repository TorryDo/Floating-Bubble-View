package com.torrydo.floatingbubble.main_icon

import android.view.LayoutInflater
import com.torrydo.floatingbubble.FloatingBubbleBuilder
import com.torrydo.floatingbubble.R
import com.torrydo.floatingbubble.base.BaseFloatingView
import com.torrydo.floatingbubble.databinding.IconMainBinding
import com.torrydo.floatingbubble.utils.Utils

class FloatingIcon(bubbleBuilder: FloatingBubbleBuilder) : BaseFloatingView(bubbleBuilder.context) {

    private val TAG = Utils.getTagName()

    private var binding = IconMainBinding.inflate(LayoutInflater.from(bubbleBuilder.context))

    init {
        setupDefaultLayoutParams()
        binding.homeLauncherMainIcon.setImageResource(R.drawable.ic_rounded_blue_diamond)
    }

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