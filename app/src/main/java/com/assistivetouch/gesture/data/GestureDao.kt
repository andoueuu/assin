package com.assistivetouch.gesture.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GestureDao {
    @Query("SELECT * FROM gestures ORDER BY createdAt DESC")
    suspend fun getAllGestures(): List<Gesture>

    @Query("SELECT * FROM gestures WHERE id = :id")
    suspend fun getGestureById(id: String): Gesture?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGesture(gesture: Gesture)

    @Delete
    suspend fun deleteGesture(gesture: Gesture)

    @Query("UPDATE gestures SET name = :newName WHERE id = :id")
    suspend fun renameGesture(id: String, newName: String)
}
