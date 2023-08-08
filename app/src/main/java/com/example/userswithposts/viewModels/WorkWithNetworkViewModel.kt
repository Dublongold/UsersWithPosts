package com.example.userswithposts.viewModels

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.userswithposts.network.NetworkState
import com.example.userswithposts.network.NetworkQueueEntity
import com.example.userswithposts.repositories.NetworkRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

abstract class WorkWithNetworkViewModel: ViewModel() {

    val networkState: NetworkState by inject(NetworkState::class.java)
    val networkRepository = NetworkRepository()

    private val networkQueue: StateFlow<List<NetworkQueueEntity<*>>>
        get() = networkRepository.networkQueue

    private var waitForConnection = false

    private fun invokeFirstNetworkQueue() {
        viewModelScope.launch {
            if(!networkRepository.invokeFirstNetworkQueue()) {
                waitForConnection = true
            }
        }
    }

    private fun invokeLastNetworkQueue() {
        viewModelScope.launch {
            if(!networkRepository.invokeLastNetworkQueue()) {
                waitForConnection = true
            }
        }
    }

    private fun watchNetworkQueue(viewLifecycleOwner: LifecycleOwner) {
        Log.i("watchNetworkQueue", "Try watch network queue.")
        viewLifecycleOwner.lifecycleScope.launch {
            networkQueue.collect {
                if(it.isNotEmpty()) {
                    Log.i("watchNetworkQueue", "Network queue changed.")
                    invokeLastNetworkQueue()
                }
            }
        }
    }

    private fun watchNetworkAvailableState(viewLifecycleOwner: LifecycleOwner) {
        Log.i("watchNetworkQueue", "Try watch network isAvailable.")
        viewLifecycleOwner.lifecycleScope.launch {
            networkState.isAvailable.collect {
                if(it && waitForConnection) {
                    Log.i("watchNetworkAvailableState", "Network is available.")
                    waitForConnection = false
                    invokeFirstNetworkQueue()
                }
            }
        }
    }

    fun configureViewNetworkObservers(viewLifecycleOwner: LifecycleOwner) {
        watchNetworkAvailableState(viewLifecycleOwner)
        watchNetworkQueue(viewLifecycleOwner)
    }

    fun clearNetworkQueue() {
        networkRepository.clearNetworkQueues()
    }
}