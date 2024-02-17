package com.teovladusic.core.common.extension

import android.util.Log
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateOrNull(): LocalDate? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        ZonedDateTime.parse(this, formatter).toLocalDate()
    } catch (e: Exception) {
        Log.e("DateTimeExtension", "toDomain: ${e.message}", e)
        null
    }
}

fun String.toLocalDateTimeOrNull(): LocalDateTime? {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        ZonedDateTime.parse(this, formatter).toLocalDateTime()
    } catch (e: Exception) {
        Log.e("DateTimeExtension", "toDomain: ${e.message}", e)
        null
    }
}
