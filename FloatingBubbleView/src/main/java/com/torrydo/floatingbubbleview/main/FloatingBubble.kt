package com.torrydo.floatingbubbleview.main

import com.torrydo.floatingbubbleview.main.bubble.FloatingBubbleIcon
import com.torrydo.floatingbubbleview.main.bubble.FloatingRemoveBubbleIcon
import com.torrydo.floatingbubbleview.physics.FloatingBubbleTouchListener
import com.torrydo.floatingbubbleview.utils.Constant
import com.torrydo.floatingbubbleview.utils.ScreenInfo
import com.torrydo.floatingbubbleview.utils.getXYPointOnScreen
import com.torrydo.floatingbubbleview.utils.logger.Logger
import com.torrydo.floatingbubbleview.utils.toTag

class FloatingBubble(
    private val bubbleBuilder: FloatingBubbleBuilder
) {

    private val logger = Logger()
        .setTag(javaClass.simpleName.toTag())
        .setDebugEnabled(Constant.IS_DEBUG_ENABLED)

    private inner class CustomBubbleTouchListener: FloatingBubbleTouchListener{

        private var isBubbleMoving = false

        override fun onMove(x: Int, y: Int) {
            if(isBubbleMoving) return
            showRemoveIcon()
            isBubbleMoving = true
        }

        override fun onUp(x: Int, y: Int) {

            removeRemoveIcon()
            isBubbleMoving = false

            stopServiceIfSuitableCondition()

        }
    }

    private var floatingIcon: FloatingBubbleIcon = FloatingBubbleIcon(
        bubbleBuilder.addFloatingBubbleTouchListener(CustomBubbleTouchListener()),
        ScreenInfo.getScreenSize(bubbleBuilder.context!!)
    )

    private var floatingRemoveIcon: FloatingRemoveBubbleIcon = FloatingRemoveBubbleIcon(
        bubbleBuilder,
        ScreenInfo.getScreenSize(bubbleBuilder.context!!)
    )



    // public func ---------------------------------------------------------------------------------

    fun showIcon() {
        floatingIcon.show()
    }

    fun removeIcon() {
        floatingIcon.remove()
    }

    fun showRemoveIcon(){
        floatingRemoveIcon.show()
    }

    fun removeRemoveIcon(){
        floatingRemoveIcon.remove()
    }


    // private func --------------------------------------------------------------------------------

    private fun stopServiceIfSuitableCondition(): Boolean {
        // get X and Y of binIcon
        val arrBin = floatingRemoveIcon.binding.homeLauncherMainBinIcon.getXYPointOnScreen()

        val binXmin = arrBin.x - 150
        val binXmax = arrBin.x + 150

        val binYmin = arrBin.y - 150
        val binYmax = arrBin.y + 150

        // get X and Y of Main Icon
        val iconArr = floatingIcon.binding.homeLauncherMainIcon.getXYPointOnScreen()

        val currentIconX = iconArr.x
        val currentIconY = iconArr.y

        if (
            binXmin < currentIconX && currentIconX < binXmax
            &&
            binYmin < currentIconY && currentIconY < binYmax
        ) {
            bubbleBuilder.listener?.onDestroy()
            logger.log("destroy service")
            return true
        }

        floatingIcon.animateIconToEdge(68){}

        return false
    }


}