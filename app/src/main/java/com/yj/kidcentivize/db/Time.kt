package com.yj.kidcentivize.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Time(
        @PrimaryKey val id: String,
        @ColumnInfo(name = "time") val time: LocalDateTime
)