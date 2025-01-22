package com.voyantiq.app

object DevModeManager {
    var isDevModeEnabled = false

    fun enableDevMode() {
        isDevModeEnabled = true
    }

    fun disableDevMode() {
        isDevModeEnabled = false
    }
}