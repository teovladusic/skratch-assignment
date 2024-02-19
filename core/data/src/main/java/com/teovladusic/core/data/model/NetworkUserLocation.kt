package com.teovladusic.core.data.model

data class NetworkUserLocation(
    val street: NetworkUserStreet,
    val city: String,
    val state: String,
    val country: String,
    val coordinates: NetworkUserCoordinates,
)
