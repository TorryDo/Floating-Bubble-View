package com.torrydo.floatingbubbleview


class PermissionDeniedException : Exception() {
    override val message: String
        get() = "\"display over other app\" permission IS NOT granted!"
}




