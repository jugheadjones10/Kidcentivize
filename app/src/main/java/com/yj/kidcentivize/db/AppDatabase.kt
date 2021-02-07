package com.yj.kidcentivize.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(User::class, Block::class, Task::class, FoodTask::class), version = 1)
@TypeConverters(Converters::class)
abstract class  AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun blockDao(): BlockDao
    abstract fun taskDao(): TaskDao
    abstract fun foodTaskDao(): FoodTaskDao
}