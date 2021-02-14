package com.yj.kidcentivize.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

@Dao
interface TimeDao {
    @Query("SELECT * FROM time LIMIT 1")
    fun getTime(): LiveData<Time>

    @Query("SELECT * FROM time LIMIT 1")
    suspend fun getTimeAsync(): Time

    @Update
    fun updateTime(time: Time)
}
