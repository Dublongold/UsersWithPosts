package com.example.userswithposts.viewModels

import com.example.userswithposts.models.User
import com.example.userswithposts.models.receivers.UsersReceiver
import com.example.userswithposts.repositories.NetworkRepository
import com.example.userswithposts.repositories.UsersListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

class UserListViewModel: WorkWithNetworkViewModel() {
    private val _isSearchMode: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val usersRepository = UsersListRepository()

    val usersList: StateFlow<List<User?>>
        get() = usersRepository.usersList

    val isSearchMode: StateFlow<Boolean>
        get() = _isSearchMode

    fun loadUsers(goodCallback: (() -> Unit)?, badCallback: (() -> Unit)? = null) {
        networkRepository.addNetworkQueueEntity(usersRepository.getLoadUsersNetworkQueueEntity(
                goodCallback = goodCallback,
                badCallback = badCallback
            )
        )
    }
    fun loadMoreUsers(goodCallback: (() -> Unit)? = null, badCallback: (() -> Unit)? = null) {
        networkRepository.addNetworkQueueEntity(usersRepository.getLoadMoreUsersNetworkQueueEntity(
                goodCallback = goodCallback,
                badCallback = badCallback
            )
        )
    }

    fun setSearchMode(value: Boolean) {
        _isSearchMode.value = value
    }

    fun searchUsers(
        query: String?,
        onSuccessful: (Response<UsersReceiver>) -> Unit,
        badCallback: (() -> Unit)? = null
    ) {
        networkRepository.addNetworkQueueEntity(
            usersRepository.getSearchUsersNetworkQueueEntity(
                query = query,
                onSuccessful = onSuccessful,
                badCallback = badCallback
            )
        )
    }
}