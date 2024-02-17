package com.teovladusic.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime

@Parcelize
data class Friend(
    val id: String,
    val name: String,
    val lastname: String,
    val gender: String,
    val pictureUrl: String,
    val username: String,
    val age: Int,
    val birthday: LocalDate?,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val phone: String,
    val email: String,
    val registeredDateTime: LocalDateTime?,
    val latitude: Double,
    val longitude: Double
): Parcelable