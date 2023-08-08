package com.example.userswithposts.viewModels

import com.example.userswithposts.models.Post
import com.example.userswithposts.models.receivers.PostsReceiver
import com.example.userswithposts.repositories.PostsListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

class PostsListViewModel: WorkWithNetworkViewModel() {
    private val _isSearchMode: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val repository = PostsListRepository()

    val postsList: StateFlow<List<Post?>>
        get() = repository.postsList

    val isSearchMode: StateFlow<Boolean>
        get() = _isSearchMode

    var userId: Int?
        get() = repository.userId
        set(value) {
            repository.userId = value
        }

    fun loadPosts(
        goodCallback: (() -> Unit)? = null,
        badCallback: (() -> Unit)? = null
    ) {
        networkRepository.addNetworkQueueEntity(repository.getLoadPostsNetworkQueueEntity(
                goodCallback = goodCallback,
                badCallback = badCallback
            )
        )
    }
    fun loadMorePosts(goodCallback: (() -> Unit)? = null, badCallback: (() -> Unit)? = null) {
        networkRepository.addNetworkQueueEntity(repository.getLoadMorePostsNetworkQueueEntity(
                goodCallback = goodCallback,
                badCallback = badCallback
            )
        )
    }

    fun setSearchMode(value: Boolean) {
        _isSearchMode.value = value
    }

    fun searchPosts(
        query: String?,
        onSuccessful: (Response<PostsReceiver>) -> Unit,
        badCallback: (() -> Unit)? = null
    ) {
        networkRepository.addNetworkQueueEntity(
            repository.getSearchPostsNetworkQueueEntity(
                query = query,
                onSuccessful = onSuccessful,
                badCallback = badCallback
            )
        )
    }
}