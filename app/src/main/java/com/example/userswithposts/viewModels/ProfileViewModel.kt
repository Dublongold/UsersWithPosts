package com.example.userswithposts.viewModels

import com.example.userswithposts.models.Post
import com.example.userswithposts.models.User
import com.example.userswithposts.repositories.NetworkRepository
import com.example.userswithposts.repositories.ProfileRepository
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : WorkWithNetworkViewModel(){

    private val repository = ProfileRepository()

    val currentUser: StateFlow<User?>
        get() = repository.currentUser

    val usersPosts: StateFlow<List<Post>>
        get() = repository.userPosts

    fun loadUserById(
        id: Int,
        goodCallback: (() -> Unit)? = null,
        badCallback: (() -> Unit)? = null
    ) {
        networkRepository.addNetworkQueueEntity(
            repository.getLoadUserByIdNetworkQueueEntity(
                id = id,
                goodCallback = goodCallback,
                badCallback = badCallback)
        )
    }

    fun loadUserPosts(
        goodCallback: (() -> Unit)? = null,
        badCallback: (() -> Unit)? = null
    ) {
        networkRepository.addNetworkQueueEntity(
            repository.getLoadUserPostsNetworkQueueEntity(
                goodCallback = goodCallback,
                badCallback = badCallback
            )
        )
    }
}