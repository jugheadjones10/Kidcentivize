package com.yj.kidcentivize.db

import androidx.room.*
import java.time.LocalDateTime

@Entity
data class Block(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "expiryTime") val expiryTime: Int,
    @ColumnInfo(name = "date") val date: LocalDateTime,
    @ColumnInfo(name = "reward") val reward: Int
)

data class BlockWithFoodTasks(
    @Embedded val block: Block,
    @Relation(
        parentColumn = "id",
        entityColumn = "blockId"
    )
    val foodTasks: List<FoodTask>
)