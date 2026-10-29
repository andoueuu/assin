package com.assistivetouch.gesture.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class AssistiveTouchService : AccessibilityService() {
    companion object {
        private var instance: AssistiveTouchService? = null
        fun getInstance(): AssistiveTouchService? = instance
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not needed for gesture replay
    }

    override fun onInterrupt() {
        // Handle interrupt
    }

    override fun onUnbind(intent: Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }
}
