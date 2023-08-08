package com.example.userswithposts.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.widget.Toast
import org.koin.java.KoinJavaComponent.inject

class MainNetworkCallback(
    private val context: Context
): ConnectivityManager.NetworkCallback() {
    private val networkState: NetworkState by inject(NetworkState::class.java)

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        if(!networkState.isAvailable.value) {
            Toast.makeText(context, "Internet connection restored.", Toast.LENGTH_SHORT).show()
        }
        networkState.setState(true)
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkState.setState(false)
        Toast.makeText(context, "Internet connection is lost.", Toast.LENGTH_SHORT).show()
    }
}