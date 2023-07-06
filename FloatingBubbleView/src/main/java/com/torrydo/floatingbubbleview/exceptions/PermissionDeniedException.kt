package com.torrydo.floatingbubbleview.exceptions


class PermissionDeniedException(private val m: String? = null) : Exception() {
    override val message: String
        get() = m ?: "\"display over other app\" permission IS NOT granted!"
}




