package com.example.userswithposts.models

import kotlinx.serialization.Serializable

@Serializable
data class Post (
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int,
    val tags: List<String>,
    val reactions: Int = 0
)

/*
Example:

{
  "id": 1,
  "title": "His mother had always taught him",
  "body": "His mother had always taught him not to ever think of himself as better than others. He'd tried to live by this motto. He never looked down on those who were less fortunate or who had less money than him. But the stupidity of the group of people he was talking to made him change his mind.",
  "userId": 9,
  "tags": [
    "history",
    "american",
    "crime"
  ],
  "reactions": 2
}
 */