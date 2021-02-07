package com.yj.kidcentivize.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey val id: String,
    val title: String,
    val fileUrl: String,
    val instructions: String,
    val theKidShould: String
)

@Entity
data class FoodTask(
    @PrimaryKey val id: String,
    val blockId: String,
    val done: Boolean,
    val submissionUrl: String,
    @Embedded val task: EmbeddedTask
)

data class EmbeddedTask(
    val taskId: String,
    val title: String,
    val fileUrl: String,
    val instructions: String,
    val theKidShould: String
)


