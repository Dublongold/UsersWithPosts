package com.example.userswithposts.viewModels

import com.example.userswithposts.models.Post
import com.example.userswithposts.models.User
import com.example.userswithposts.repositories.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PostViewModel: WorkWithNetworkViewModel() {
    private var _postId: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val repository = PostRepository()

    val postId: StateFlow<Int?>
        get() = _postId

    val currentPost: StateFlow<Post?>
        get() = repository.currentPost

    val postAuthor: StateFlow<User?>
        get() = repository.postAuthor

    fun setPostId(value: Int) {
        _postId.value = value
        repository.postId = value
    }

    fun loadPost(
        goodCallback: (() -> Unit)? = null
    ) {
        networkRepository.addNetworkQueueEntity(
            repository.getLoadPostNetworkQueueEntity(
                goodCallback = goodCallback
            )
        )
    }

    fun loadAuthor() {
        networkRepository.addNetworkQueueEntity(
            repository.getLoadAuthorNetworkQueueEntity()
        )
    }
}