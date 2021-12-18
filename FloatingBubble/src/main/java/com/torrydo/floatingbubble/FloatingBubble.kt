package com.torrydo.floatingbubble

import com.torrydo.floatingbubble.main_icon.FloatingBubbleIcon

class FloatingBubble(
    bubbleBuilder: FloatingBubbleBuilder
) {

    var floatingIcon = FloatingBubbleIcon(bubbleBuilder)

    fun show() {
        floatingIcon.show()
    }

    fun remove() {
        floatingIcon.remove()
    }


}