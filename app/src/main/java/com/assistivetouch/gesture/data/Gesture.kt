package com.assistivetouch.gesture.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "gestures")
data class Gesture(
    @PrimaryKey val id: String,
    val name: String,
    val eventsJson: String,
    val duration: Long,
    val orientation: Int, // 0 = Portrait, 1 = Landscape
    val screenWidth: Int,
    val screenHeight: Int,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun getEvents(): List<GestureEvent> {
        val type = object : TypeToken<List<GestureEvent>>() {}.type
        return Gson().fromJson(eventsJson, type) ?: emptyList()
    }

    companion object {
        fun fromEvents(
            name: String,
            events: List<GestureEvent>,
            duration: Long,
            orientation: Int,
            screenWidth: Int,
            screenHeight: Int
        ): Gesture {
            return Gesture(
                id = System.currentTimeMillis().toString(),
                name = name,
                eventsJson = Gson().toJson(events),
                duration = duration,
                orientation = orientation,
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        }
    }
}

data class GestureEvent(
    val x: Float,
    val y: Float,
    val timestamp: Long,
    val action: Int
) {
    companion object {
        const val ACTION_DOWN = 0
        const val ACTION_MOVE = 1
        const val ACTION_UP = 2
    }
}
