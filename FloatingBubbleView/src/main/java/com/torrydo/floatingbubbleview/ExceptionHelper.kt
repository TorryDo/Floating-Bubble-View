package com.torrydo.floatingbubbleview

import android.util.Log

//private val <T : Any > T.classNameJava: String
//    get() {
//        return javaClass.simpleName
//    }

fun String?.addPrefix() = "<> $this"

sealed class ActionState {
    class ActionComplete : ActionState()
    class ActionError(val e: Exception) : ActionState()
}

// use ---------------------------------------------------------------------------------------------

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
        ActionState.ActionError(e)
    }

    return ActionState.ActionComplete()
}

inline fun <T : Any> T.logIfError(
    tag: String? = javaClass.simpleName.toString(),
    crossinline mayErrorWork: () -> Unit,
): ActionState {
    try {
        mayErrorWork()
    } catch (e: Exception) {
        Log.e(tag.addPrefix(), e.message.toString())
        ActionState.ActionError(e)
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

