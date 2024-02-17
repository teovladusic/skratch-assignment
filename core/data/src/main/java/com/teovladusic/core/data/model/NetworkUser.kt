package com.teovladusic.core.data.model

import com.teovladusic.core.common.extension.toLocalDateOrNull
import com.teovladusic.core.common.extension.toLocalDateTimeOrNull
import com.teovladusic.core.domain.model.Friend

data class NetworkUser(
    val gender: String,
    val name: NetworkUserName,
    val location: NetworkUserLocation,
    val email: String,
    val phone: String,
    val picture: NetworkUserPicture,
    val login: NetworkUserLogin,
    val dob: NetworkUserDob,
    val registered: NetworkUserRegistered
)

fun NetworkUser.toDomain(): Friend {
    return Friend(
        id = login.uuid,
        name = name.first,
        lastname = name.last,
        gender = gender,
        pictureUrl = picture.large,
        username = login.username,
        age = dob.age,
        birthday = dob.date.toLocalDateOrNull(),
        address = location.street.run { "$number $name" },
        city = location.city,
        country = location.country,
        state = location.state,
        phone = phone,
        email = email,
        registeredDateTime = registered.date.toLocalDateTimeOrNull(),
        latitude = location.coordinates.latitude,
        longitude = location.coordinates.longitude
    )
}