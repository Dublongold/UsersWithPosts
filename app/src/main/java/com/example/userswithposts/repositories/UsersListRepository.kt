package com.example.userswithposts.repositories

import com.example.userswithposts.models.User
import com.example.userswithposts.models.receivers.UsersReceiver
import com.example.userswithposts.network.NetworkQueueEntity
import com.example.userswithposts.retrofit.NetworkApi
import com.example.userswithposts.retrofit.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Response

class UsersListRepository {

    private val networkClient: NetworkApi by inject(NetworkApi::class.java)
    private val _usersList: MutableStateFlow<List<User?>> = MutableStateFlow(emptyList())

    val usersList: StateFlow<List<User?>>
        get() = _usersList

    fun getLoadUsersNetworkQueueEntity(
        goodCallback: (() -> Unit)?,
        badCallback: (() -> Unit)?
    ) = NetworkQueueEntity(
            id = 1,
            workCondition = {_usersList.value.isEmpty()},
            response = { networkClient.getUsers() },
            onSuccessful = {response ->
            val newUsers = response.body()!!.users
                _usersList.value = if(newUsers.size < 30) {
                    newUsers
                }
                else {
                    newUsers + null
                }
            },
            goodCallback = goodCallback,
            badCallback = badCallback
        )

    fun getLoadMoreUsersNetworkQueueEntity(
        goodCallback: (() -> Unit)? = null,
        badCallback: (() -> Unit)? = null
    ) = NetworkQueueEntity(
            id = 2,
            response = { networkClient.getUsers(skip = usersList.value.filterNotNull().size) },
            onSuccessful = {response -> _usersList.value =
                if (_usersList.value.size + response.body()!!.users.size < response.body()!!.total) {
                    _usersList.value.filterNotNull() + response.body()!!.users + null
                } else {
                    _usersList.value.filterNotNull() + response.body()!!.users
                }},
            goodCallback = goodCallback,
            badCallback = badCallback
        )
    fun getSearchUsersNetworkQueueEntity(
        query: String?,
        onSuccessful: (Response<UsersReceiver>) -> Unit,
        badCallback: (() -> Unit)? = null
    ) = NetworkQueueEntity(
            id = 4,
            workCondition = {
                !query.isNullOrEmpty()
            },
            response = {
                networkClient.searchUsers(query!!)
            },
            onSuccessful = onSuccessful,
            badCallback = badCallback,
            retryIfError = false
        )
}