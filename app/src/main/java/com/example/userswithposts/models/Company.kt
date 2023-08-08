package com.example.userswithposts.models

import kotlinx.serialization.Serializable

@Serializable
data class Company (
    val address: Address?,
    val department: String?,
    val name: String?,
    val title: String?
)