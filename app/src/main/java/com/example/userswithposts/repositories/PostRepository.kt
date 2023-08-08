package com.example.userswithposts.repositories

import com.example.userswithposts.models.Post
import com.example.userswithposts.models.User
import com.example.userswithposts.network.NetworkQueueEntity
import com.example.userswithposts.retrofit.NetworkApi
import com.example.userswithposts.retrofit.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent

class PostRepository {
    private val networkClient: NetworkApi by KoinJavaComponent.inject(NetworkApi::class.java)

    private var _currentPost: MutableStateFlow<Post?> = MutableStateFlow(null)
    private var _postAuthor: MutableStateFlow<User?> = MutableStateFlow(null)

    var postId: Int? = null

    val currentPost: StateFlow<Post?>
        get() = _currentPost

    val postAuthor: StateFlow<User?>
        get() = _postAuthor

    fun getLoadPostNetworkQueueEntity(
        goodCallback: (() -> Unit)? = null
    ): NetworkQueueEntity<Post> {
        return NetworkQueueEntity(
            id = 6,
            workCondition = {postId != null},
            throwUnableConnectIfFalse = true,
            response = { networkClient.getPostById(postId!!)},
            onSuccessful = {_currentPost.value = it.body()!!},
            goodCallback = goodCallback
        )
    }

    fun getLoadAuthorNetworkQueueEntity(): NetworkQueueEntity<User> {
        return NetworkQueueEntity(
            id = 4,
            workCondition = {_currentPost.value != null},
            throwUnableConnectIfFalse = true,
            response = { networkClient.getUserById(_currentPost.value!!.userId)},
            onSuccessful = {_postAuthor.value = it.body()!!}
        )
    }
}