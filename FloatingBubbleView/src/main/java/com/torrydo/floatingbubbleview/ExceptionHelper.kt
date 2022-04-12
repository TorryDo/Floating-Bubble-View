package com.torrydo.floatingbubbleview

import android.util.Log


fun String?.addPrefix() = "<> $this"

sealed class ActionState {
    class ActionComplete : ActionState()
    class ActionError(val e: Exception) : ActionState()
}

// use ---------------------------------------------------------------------------------------------

/**
 * if error appear, do nothing
 * */
inline fun tryOnly(crossinline mayErrorWork: () -> Unit) {
    try {
        mayErrorWork()
    } catch (e: Exception) {
    }
}

inline fun mayError(
    crossinline mayErrorWork: () -> Unit,
): ActionState {
    try {
        mayErrorWork()
    } catch (e: Exception) {
        return ActionState.ActionError(e)
    }

    return ActionState.ActionComplete()
}

/**
 * if error appear, print to console
 * */
inline fun <T : Any> T.logIfError(
    tag: String? = javaClass.simpleName.toString(),
    crossinline mayErrorWork: () -> Unit,
): ActionState {
    try {
        mayErrorWork()
    } catch (e: Exception) {
        Log.e(tag.addPrefix(), e.message.toString())
        return ActionState.ActionError(e)
    }

    return ActionState.ActionComplete()
}

// extension ---------------------------------------------------------------------------------------

inline fun ActionState.onComplete(crossinline onIfComplete: () -> Unit): ActionState {

    if (this is ActionState.ActionComplete) {
        onIfComplete()
    }
    return this
}

inline fun ActionState.onError(crossinline onIfError: (e: Exception) -> Unit): ActionState {

    if (this is ActionState.ActionError) {
        onIfError(this.e)
    }
    return this
}

// custom exception

class PermissionDeniedException: Exception(){
    override val message: String
        get() = "display-over-other-app permission IS NOT granted!"
}




