package com.example.userswithposts

import com.example.userswithposts.models.Post
import com.example.userswithposts.models.receivers.PostsReceiver
import com.example.userswithposts.retrofit.RetrofitClient
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test
import retrofit2.Retrofit

class TestPosts {
    @Test
    fun testPostsReceiverConvert() {
        val str = """
            {
              "posts": [
                {
                  "id": 7,
                  "title": "This is important to remember.",
                  "body": "This is important to remember. Love isn't like pie. You don't need to divide it among all your friends and loved ones. No matter how much love you give, you can always give more. It doesn't run out, so don't try to hold back giving it as if it may one day run out. Give it freely and as much as you want.",
                  "userId": 12,
                  "tags": [
                    "magical",
                    "crime"
                  ],
                  "reactions": 0
                }
              ],
              "total": 11,
              "skip": 0,
              "limit": 11
            }"""
        val post = Json.decodeFromString<PostsReceiver?>(str)
        assert(post != null)
    }

    @Test
    fun testPostConvert() {
        val str = """ 
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
            }"""
        val post = Json.decodeFromString<Post?>(str)
        assert(post != null)
    }

    @Test
    fun testLoadPosts() {
        runBlocking {
            val postsReceiver = RetrofitClient.getPostsByUserId(1)
            assert(postsReceiver.isSuccessful && (postsReceiver.body()!!.posts.isEmpty() || postsReceiver.body()!!.posts.firstOrNull()?.userId == 1))
        }
    }

    @Test
    fun testLoadSinglePost() {
        runBlocking {
            val post = RetrofitClient.getPostById(1)
            assert(post.isSuccessful && post.body()!!.id == 1)
        }
    }
}