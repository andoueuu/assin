package com.assistivetouch.gesture.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_actions")
data class CustomAction(
    @PrimaryKey val actionType: String, // "single_tap", "double_tap", "long_press"
    val gestureId: String?,
    val actionName: String
)
