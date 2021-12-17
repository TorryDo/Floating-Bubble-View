package com.torrydo.floatingbubble

import com.torrydo.floatingbubble.main_icon.FloatingIcon

class FloatingBubble(
    bubbleBuilder: FloatingBubbleBuilder
) {

    var floatingIcon = FloatingIcon(bubbleBuilder)

    fun show() {
        floatingIcon.show()
    }

    fun remove() {
        floatingIcon.remove()
    }


}