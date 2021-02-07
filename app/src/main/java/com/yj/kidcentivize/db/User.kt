package com.yj.kidcentivize.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey val id: String,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "code") val code: String,
        @ColumnInfo(name = "timeUsed") val timeUsed: Int? = null,
        @ColumnInfo(name = "timeRemaining") val timeRemaining: Int? = null,
        @ColumnInfo(name = "kids") val kids: String? = null
)