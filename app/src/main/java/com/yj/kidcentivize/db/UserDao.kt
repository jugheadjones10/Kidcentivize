package com.yj.kidcentivize.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user")
    suspend fun getAllAsync(): List<User>

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getOneAsync(): User

    @Query("SELECT timeRemaining FROM user LIMIT 1")
    suspend fun getTimeRemaining(): Int

    @Query("SELECT * FROM user LIMIT 1")
    fun getOne(): LiveData<User>

    @Insert
    suspend fun insertUser(user: User)

//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllByIds(userIds: IntArray): List<User>
//
//    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User
//
//    @Insert
//    fun insertAll(vararg users: User)
//
    @Query("DELETE FROM user")
    fun deleteAll()
}