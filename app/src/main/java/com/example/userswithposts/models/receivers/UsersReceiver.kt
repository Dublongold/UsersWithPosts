package com.example.userswithposts.models.receivers

import com.example.userswithposts.models.User
import kotlinx.serialization.Serializable

@Serializable
data class UsersReceiver (
    val users: List<User>,
    val total: Int,
    val skip: Int,
    val limit: Int
)