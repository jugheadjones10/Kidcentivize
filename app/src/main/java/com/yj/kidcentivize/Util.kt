package com.yj.kidcentivize

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun dateToLocalDateTime(date: Date): LocalDateTime {
    return Instant.ofEpochMilli( date.time )
        .atZone( ZoneId.systemDefault() )
        .toLocalDateTime()
}