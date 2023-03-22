package com.torrydo.floatingbubbleview

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.AbstractComposeView

internal class FloatingComposeView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbstractComposeView(context, attrs, defStyleAttr) {

    companion object {
        internal var keyEventHandler: ((KeyEvent?) -> Boolean?)? = null
    }

    private val content = mutableStateOf<(@Composable () -> Unit)?>(null)


    @Suppress("RedundantVisibilityModifier")
    protected override var shouldCreateCompositionOnAttachedToWindow: Boolean = false

    @Composable
    override fun Content() {
        content.value?.invoke()
    }

    override fun getAccessibilityClassName(): CharSequence {
        return javaClass.name
    }

    /**
     * Set the Jetpack Compose UI content for this view.
     * Initial composition will occur when the view becomes attached to a window or when
     * [createComposition] is called, whichever comes first.
     */
    fun setContent(content: @Composable () -> Unit) {
        shouldCreateCompositionOnAttachedToWindow = true
        this.content.value = content
        if (isAttachedToWindow) {
            createComposition()
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val b = keyEventHandler?.invoke(event)
        return b ?: super.dispatchKeyEvent(event)
    }

}