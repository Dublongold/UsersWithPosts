package com.example.userswithposts.retrofit

import com.example.userswithposts.models.Post
import com.example.userswithposts.models.User
import com.example.userswithposts.models.receivers.PostsReceiver
import com.example.userswithposts.models.receivers.UsersReceiver
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {
    @GET("users")
    suspend fun getUsers(@Query("skip") skip: Int = 0, @Query("limit") limit: Int = 30): Response<UsersReceiver>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<User>

    @GET("posts/user/{id}")
    suspend fun getPostsByUserId(@Path("id") id: Int, @Query("skip") skip: Int = 0): Response<PostsReceiver>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Response<Post>

    @GET("users/search")
    suspend fun searchUsers(
        @Query("q") query: String): Response<UsersReceiver>

    @GET("posts/search")
    suspend fun searchPosts(
        @Query("q") query: String): Response<PostsReceiver>
}