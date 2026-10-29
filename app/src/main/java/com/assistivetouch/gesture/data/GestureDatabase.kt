package com.assistivetouch.gesture.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Gesture::class, CustomAction::class], version = 1, exportSchema = false)
abstract class GestureDatabase : RoomDatabase() {
    abstract fun gestureDao(): GestureDao
    abstract fun customActionDao(): CustomActionDao

    companion object {
        @Volatile
        private var INSTANCE: GestureDatabase? = null

        fun getDatabase(context: Context): GestureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GestureDatabase::class.java,
                    "gesture_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
