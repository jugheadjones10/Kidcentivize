package com.yj.kidcentivize.api

import com.squareup.moshi.JsonClass
import com.yj.kidcentivize.db.User

@JsonClass(generateAdapter = true)
data class Kid (
    val id: String,
    val name: String,
    val kidCode: String,
    val timeUsed: Int,
    val timeRemaining: Int
)

fun Kid.asDatabaseModel(): User {
    return User(
        id = this.id,
        name = this.name,
        code = this.kidCode,
        timeUsed = this.timeUsed,
        timeRemaining = this.timeRemaining
    )
}

@JsonClass(generateAdapter = true)
data class Parent (
    val id: String,
    val name: String,
    val kidCode: String,
    val kids: List<String>
)

fun Parent.asDatabaseModel(): User {
    return User(
        id = this.id,
        name = this.name,
        code = this.kidCode,
        kids = this.kids.first()
    )
}


data class Task(
    val id: String
)

