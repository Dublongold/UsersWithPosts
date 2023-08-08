package com.example.userswithposts.repositories

import android.util.Log
import com.example.userswithposts.network.NetworkState
import com.example.userswithposts.network.NetworkQueueEntity
import com.example.userswithposts.network.NetworkQueueState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import org.koin.java.KoinJavaComponent.inject

class NetworkRepository {

    private val mutex= Mutex()
    private val networkState: NetworkState by inject(NetworkState::class.java)

    private var _networkQueue: MutableStateFlow<List<NetworkQueueEntity<*>>> =
        MutableStateFlow(emptyList())

    val networkQueue: StateFlow<List<NetworkQueueEntity<*>>>
        get() = _networkQueue
    // If false is returned, then viewModel assign true to "waitConnection".
    suspend fun invokeLastNetworkQueue(): Boolean {
        return if(_networkQueue.value.isNotEmpty()) {
            invokeNetworkQueue(_networkQueue.value.last())
        }
        else {
            true
        }
    }
    // If false is returned, then viewModel assign true to "waitConnection".
    suspend fun invokeFirstNetworkQueue(): Boolean {
        return if(_networkQueue.value.isNotEmpty()) {
            invokeNetworkQueue(_networkQueue.value.first())
        }
        else {
            true
        }
    }
    // If false is returned, then viewModel assign true to "waitConnection".
    private suspend fun invokeNetworkQueue(entity: NetworkQueueEntity<*>): Boolean {
        if(mutex.isLocked) {
            Log.i("invokeNetworkQueue", "Try to invoke network queue with id ${entity.id}, but mutex is locked.")
            return true
        }
        mutex.lock()
        Log.i("invokeNetworkQueue", "Invoke network queue with id ${entity.id}")
        when (entity.invokeQueue()) {
            NetworkQueueState.TIME_OUT -> {
                Log.i("invokeNetworkQueue", "NQE with id ${entity.id} state is TIME_OUT")
                invokeNextNetworkQueue(entity.retryIfError.not())
            }

            NetworkQueueState.COMPLETED -> {
                Log.i("invokeNetworkQueue", "NQE with id ${entity.id} state is COMPLETED")
                invokeNextNetworkQueue()
            }

            NetworkQueueState.UNABLE_CONNECT -> {
                Log.i("invokeNetworkQueue", "NQE with id ${entity.id} state is UNABLE_CONNECT")
                if(mutex.isLocked)
                    mutex.unlock()
                return false
            }

            else -> {
                Log.i("invokeNetworkQueue", "NQE with id ${entity.id} state is ELSE")
            }
        }
        Log.i("invokeNetworkQueue", "End of invoke network queue with id ${entity.id}")

        if(mutex.isLocked)
            mutex.unlock()
        return true
    }

    private fun invokeNextNetworkQueue(removeOld: Boolean = true) {
        if(removeOld) {
            val newList = _networkQueue.value.toMutableList()
            val firstId = newList.firstOrNull()?.id
            if(firstId != null) {
                while(newList.isNotEmpty() && firstId == newList.first().id) {
                    newList.removeFirst()
                }
            }
            if(mutex.isLocked)
                mutex.unlock()
            _networkQueue.value = newList
        }
    }

    fun addNetworkQueueEntity(value: NetworkQueueEntity<*>) {
        if((!networkState.isAvailable.value && !value.retryIfError).not()) {
            Log.i("addNetworkQueueEntity", "Add network queue entity with id ${value.id}")
            _networkQueue.update { it + value }
        }
        else {
            Log.i("addNetworkQueueEntity", "Invoke bad callback in network queue entity with id ${value.id}")
            value.badCallback?.invoke()
        }
    }

    fun clearNetworkQueues() {
        _networkQueue.update { emptyList() }
    }
}