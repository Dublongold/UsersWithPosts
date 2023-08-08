package com.example.userswithposts

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.userswithposts.network.MainNetworkCallback
import com.example.userswithposts.network.NetworkState
import com.example.userswithposts.retrofit.RetrofitClient
import com.example.userswithposts.util.MainOnBackPressedCallback
import com.example.userswithposts.viewModels.PostViewModel
import com.example.userswithposts.viewModels.PostsListViewModel
import com.example.userswithposts.viewModels.ProfileViewModel
import com.example.userswithposts.viewModels.UserListViewModel
import com.example.userswithposts.views.UsersListFragment
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(module {
                viewModelOf(::UserListViewModel)
                viewModelOf(::PostsListViewModel)
                viewModelOf(::ProfileViewModel)
                viewModelOf(::PostViewModel)
                single { NetworkState() }
                single { RetrofitClient().client }
            })
        }

        onBackPressedDispatcher.addCallback (MainOnBackPressedCallback(supportFragmentManager))

        getSystemService(ConnectivityManager::class.java).registerNetworkCallback(
            networkRequest, MainNetworkCallback(this)
        )

        supportFragmentManager.beginTransaction()
            .add(R.id.mainContainer, UsersListFragment())
            .commit()
    }
}