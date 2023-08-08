package com.example.userswithposts.models.receivers

import com.example.userswithposts.models.Post
import kotlinx.serialization.Serializable

@Serializable
data class PostsReceiver (
    val posts: List<Post>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

/*
Example:

{
  "posts": [
    {
      "id": 7,
      "title": "This is important to remember.",
      "body": "This is important to remember. Love isn't like pie. You don't need to divide it among all your friends and loved ones. No matter how much love you give, you can always give more. It doesn't run out, so don't try to hold back giving it as if it may one day run out. Give it freely and as much as you want.",
      // body contains "love"
      "userId": 12,
      "tags": [
        "magical",
        "crime"
      ],
      "reactions": 0
    },
    {...},
    {...},
    {...}
    // 11 results
  ],
  "total": 11,
  "skip": 0,
  "limit": 11
}
 */