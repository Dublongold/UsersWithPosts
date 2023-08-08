package com.example.userswithposts.repositories

import androidx.lifecycle.viewModelScope
import com.example.userswithposts.models.Post
import com.example.userswithposts.models.User
import com.example.userswithposts.models.receivers.PostsReceiver
import com.example.userswithposts.network.NetworkQueueEntity
import com.example.userswithposts.retrofit.NetworkApi
import com.example.userswithposts.retrofit.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class ProfileRepository {
    private val networkClient: NetworkApi by KoinJavaComponent.inject(NetworkApi::class.java)

    private var _currentUser = MutableStateFlow<User?>(null)
    private var _usersPosts: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())

    val currentUser: StateFlow<User?>
        get() = _currentUser

    val userPosts: StateFlow<List<Post>>
        get() = _usersPosts

    fun getLoadUserByIdNetworkQueueEntity(
        id: Int,
        goodCallback: (() -> Unit)? = null,
        badCallback: (() -> Unit)? = null
    ) = NetworkQueueEntity(
            id = 4,
            response = {networkClient.getUserById(id)},
            onSuccessful = {
                _currentUser.value = it.body()!!
            },
            goodCallback = goodCallback,
            badCallback = badCallback
        )

    fun getLoadUserPostsNetworkQueueEntity(
        goodCallback: (() -> Unit)? = null,
        badCallback: (() -> Unit)? = null
    ) = NetworkQueueEntity(
            id = 5,
            workCondition = {_currentUser.value != null},
            response = {networkClient.getPostsByUserId(_currentUser.value!!.id)},
            onSuccessful = {
                _usersPosts.value = it.body()!!.posts
            },
            goodCallback = goodCallback,
            badCallback = badCallback,
            throwUnableConnectIfFalse = true
        )

    fun setCurrentUser(user: User) {
        _currentUser.value = user
    }

    fun setUserPosts(posts: List<Post>) {
        _usersPosts.value = posts
    }
}