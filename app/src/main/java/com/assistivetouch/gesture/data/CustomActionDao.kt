package com.assistivetouch.gesture.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CustomActionDao {
    @Query("SELECT * FROM custom_actions")
    suspend fun getAllActions(): List<CustomAction>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: CustomAction)

    @Query("SELECT * FROM custom_actions WHERE actionType = :type")
    suspend fun getActionByType(type: String): CustomAction?
}
