package com.teovladusic.core.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Friend(
    val id: String,
    val name: String,
    val lastname: String,
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
)