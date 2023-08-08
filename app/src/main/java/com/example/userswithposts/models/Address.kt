package com.example.userswithposts.models

import kotlinx.serialization.Serializable

@Serializable
data class Address (
    val address: String?,
    val city: String? = null,
    val coordinates: Coordinates?,
    val postalCode: String?,
    val state: String?
)