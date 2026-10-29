package com.assistivetouch.gesture.overlay

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.ImageView
import com.assistivetouch.gesture.R
import com.assistivetouch.gesture.data.CustomAction
import com.assistivetouch.gesture.data.GestureDatabase
import com.assistivetouch.gesture.service.GestureReplayManager
import kotlinx.coroutines.*

class FloatingButtonService : Service() {
    private lateinit var windowManager: WindowManager
    private var floatingView: View? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var lastTapTime = 0L
    private var tapCount = 0

    companion object {
        private const val DOUBLE_TAP_TIMEOUT = 300L
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        createFloatingButton()
    }

    private fun createFloatingButton() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 100
        }

        val button = ImageView(this).apply {
            setImageResource(android.R.drawable.ic_menu_compass)
            background = getDrawable(R.drawable.circle_background)
            setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX + (event.rawX - initialTouchX).toInt()
                        params.y = initialY + (event.rawY - initialTouchY).toInt()
                        windowManager.updateViewLayout(view, params)
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        val deltaTime = System.currentTimeMillis() - lastTapTime
                        val deltaX = Math.abs(event.rawX - initialTouchX)
                        val deltaY = Math.abs(event.rawY - initialTouchY)

                        if (deltaX < 10 && deltaY < 10) {
                            tapCount++
                            if (tapCount == 1) {
                                lastTapTime = System.currentTimeMillis()
                                serviceScope.launch {
                                    delay(DOUBLE_TAP_TIMEOUT)
                                    if (tapCount == 1) {
                                        handleSingleTap()
                                    }
                                    tapCount = 0
                                }
                            } else if (tapCount == 2) {
                                handleDoubleTap()
                                tapCount = 0
                            }
                        }
                        true
                    }
                    else -> false
                }
            }
        }

        floatingView = button
        windowManager.addView(button, params)
    }

    private fun handleSingleTap() {
        serviceScope.launch {
            val db = GestureDatabase.getDatabase(this@FloatingButtonService)
            val action = db.customActionDao().getActionByType("single_tap")

            when (action?.actionName) {
                "Open Menu" -> showMenu()
                else -> {
                    action?.gestureId?.let { gestureId ->
                        val gesture = db.gestureDao().getGestureById(gestureId)
                        gesture?.let {
                            GestureReplayManager.replayGesture(this@FloatingButtonService, it)
                        }
                    }
                }
            }
        }
    }

    private fun handleDoubleTap() {
        serviceScope.launch {
            val db = GestureDatabase.getDatabase(this@FloatingButtonService)
            val action = db.customActionDao().getActionByType("double_tap")

            action?.gestureId?.let { gestureId ->
                val gesture = db.gestureDao().getGestureById(gestureId)
                gesture?.let {
                    GestureReplayManager.replayGesture(this@FloatingButtonService, it)
                }
            }
        }
    }

    private fun showMenu() {
        // Show expanded menu with gestures
        // Implementation would show a popup with gesture list
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        floatingView?.let { windowManager.removeView(it) }
        serviceScope.cancel()
    }
}
