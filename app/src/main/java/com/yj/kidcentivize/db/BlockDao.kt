package com.yj.kidcentivize.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BlockDao {
    @Transaction
    @Query("SELECT * FROM block")
    fun getBlocksWithFoodTasks(): LiveData<List<BlockWithFoodTasks>>

    @Query("SELECT * FROM block")
    fun getAll(): LiveData<List<Block>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBlock(block: Block)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBlocks(vararg blocks: Block)

    @Query("DELETE FROM block")
    fun deleteAll()
}