package com.assistivetouch.gesture.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.assistivetouch.gesture.data.Gesture
import com.assistivetouch.gesture.data.GestureEvent

object GestureReplayManager {
    private const val TAG = "GestureReplayManager"
    private var isReplaying = false
    private val handler = Handler(Looper.getMainLooper())

    fun replayGesture(context: Context, gesture: Gesture) {
        val service = AssistiveTouchService.getInstance()
        if (service == null) {
            Toast.makeText(context, "Please enable accessibility service", Toast.LENGTH_LONG).show()
            return
        }

        if (isReplaying) {
            Toast.makeText(context, "Already replaying", Toast.LENGTH_SHORT).show()
            return
        }

        isReplaying = true

        try {
            val events = gesture.getEvents()
            if (events.isEmpty()) {
                isReplaying = false
                return
            }

            val path = Path()
            var startTime = 0L
            var duration = 0L

            for (event in events) {
                when (event.action) {
                    GestureEvent.ACTION_DOWN -> {
                        path.moveTo(event.x, event.y)
                        startTime = event.timestamp
                    }
                    GestureEvent.ACTION_MOVE -> path.lineTo(event.x, event.y)
                    GestureEvent.ACTION_UP -> {
                        path.lineTo(event.x, event.y)
                        duration = event.timestamp - startTime
                    }
                }
            }

            if (duration > 0) {
                val stroke = GestureDescription.StrokeDescription(path, 0, duration)
                val gestureDesc = GestureDescription.Builder().addStroke(stroke).build()

                service.dispatchGesture(gestureDesc, object : AccessibilityService.GestureResultCallback() {
                    override fun onCompleted(gestureDescription: GestureDescription?) {
                        super.onCompleted(gestureDescription)
                        isReplaying = false
                        handler.post {
                            Toast.makeText(context, "Replay completed", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(gestureDescription: GestureDescription?) {
                        super.onCancelled(gestureDescription)
                        isReplaying = false
                        handler.post {
                            Toast.makeText(context, "Replay cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                }, handler)
            } else {
                isReplaying = false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error replaying gesture", e)
            isReplaying = false
        }
    }

    fun stopReplay() {
        isReplaying = false
    }
}
