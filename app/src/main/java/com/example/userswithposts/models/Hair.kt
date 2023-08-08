package com.example.userswithposts.models

import kotlinx.serialization.Serializable

@Serializable
data class Hair(
    val color: String?,
    val type: String?
)
