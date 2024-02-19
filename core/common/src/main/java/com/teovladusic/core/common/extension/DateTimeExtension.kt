package com.teovladusic.core.common.extension

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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

fun LocalDate.format(style: Int = DateFormat.LONG): String {
    val formatter = DateFormat.getDateInstance(style, Locale.getDefault())

    return try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = simpleDateFormat.parse(this.toString())
            ?: throw IllegalArgumentException("Cannot parse the date")

        formatter.format(date)
    } catch (e: Exception) {
        Log.e("LocalDate::format", "format: ${e.message}", e)
        this.toString()
    }
}

fun LocalDateTime.format(
    dateStyle: Int = DateFormat.LONG,
    timeStyle: Int = DateFormat.LONG
): String {
    val formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.getDefault())

    return try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val date = simpleDateFormat.parse(this.toString())
            ?: throw IllegalArgumentException("Cannot parse the date")

        formatter.format(date)
    } catch (e: Exception) {
        Log.e("LocalDate::format", "format: ${e.message}", e)
        this.toString()
    }
}