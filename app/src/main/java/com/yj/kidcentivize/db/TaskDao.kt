package com.yj.kidcentivize.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task")
    fun getAll(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Query("DELETE FROM task")
    fun deleteAll()
}


@Dao
interface FoodTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFoodTask(foodTask: FoodTask)

    @Query("DELETE FROM foodtask")
    fun deleteAll()
}