package com.example.userswithposts.repositories

import com.example.userswithposts.models.Post
import com.example.userswithposts.models.User
import com.example.userswithposts.models.receivers.PostsReceiver
import com.example.userswithposts.models.receivers.UsersReceiver
import com.example.userswithposts.network.NetworkQueueEntity
import com.example.userswithposts.retrofit.NetworkApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Response

class PostsListRepository {

    private val networkClient: NetworkApi by inject(NetworkApi::class.java)
    private val _postsList: MutableStateFlow<List<Post?>> = MutableStateFlow(emptyList())

    val postsList: StateFlow<List<Post?>>
        get() = _postsList

    var userId: Int? = null

    fun getLoadPostsNetworkQueueEntity(
        goodCallback: (() -> Unit)?,
        badCallback: (() -> Unit)?
    ) = NetworkQueueEntity(
            id = 7,
            workCondition = {_postsList.value.isEmpty() && userId != null},
            response = { networkClient.getPostsByUserId(userId!!) },
        onSuccessful = {response ->
            val newPosts = response.body()!!.posts
            _postsList.value = if(newPosts.size < 30) {
                newPosts
            }
            else {
                newPosts + null
            }
        },
            goodCallback = goodCallback,
            badCallback = badCallback
        )

    fun getLoadMorePostsNetworkQueueEntity(
        goodCallback: (() -> Unit)? = null,
        badCallback: (() -> Unit)? = null
    ) = NetworkQueueEntity(
            id = 8,
            workCondition = {userId != null},
            response = { networkClient.getPostsByUserId(id = userId!!,skip = _postsList.value.filterNotNull().size) },
            onSuccessful = {response -> _postsList.value =
                if (_postsList.value.size + response.body()!!.posts.size < response.body()!!.total) {
                    _postsList.value.filterNotNull() + response.body()!!.posts + null
                } else {
                    _postsList.value.filterNotNull() + response.body()!!.posts
                }},
            goodCallback = goodCallback,
            badCallback = badCallback
        )
    fun getSearchPostsNetworkQueueEntity(
        query: String?,
        onSuccessful: (Response<PostsReceiver>) -> Unit,
        badCallback: (() -> Unit)? = null
    ) = NetworkQueueEntity(
            id = 9,
            workCondition = {
                !query.isNullOrEmpty()
            },
            response = {
                networkClient.searchPosts(query!!)
            },
            onSuccessful = onSuccessful,
            badCallback = badCallback,
            retryIfError = false
        )
}