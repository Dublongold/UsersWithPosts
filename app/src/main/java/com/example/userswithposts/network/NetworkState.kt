package com.example.userswithposts.network

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.userswithposts.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NetworkState {
    private var _isAvailable: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val isAvailable
        get() = _isAvailable

    fun setState(value: Boolean) {
        _isAvailable.value = value
    }

    fun watchIsAvailableForConnectionLostIcon(
        lifecycleOwner: LifecycleOwner,
        view: View
    ) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                isAvailable.collect {
                    if(it) {
                        view.findViewById<ImageView>(R.id.connectionLostImage).visibility = View.GONE
                    }
                    else {
                        view.findViewById<ImageView>(R.id.connectionLostImage).visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}