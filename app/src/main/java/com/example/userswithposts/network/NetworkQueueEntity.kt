package com.example.userswithposts.network

import org.koin.java.KoinJavaComponent.inject
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/* Id indicates which network request this is.
This is necessary to remove the same network requests that follow it and are unnecessary.
The id and type of network request:

1 - first load users.
2 - load more users.
3 - search users.
4 - load a user.
5 - load user posts with limit 4.
6 - load a post.
7 - load user's posts.
8 - load more user's posts.
9 - search user's posts.
*/

class NetworkQueueEntity<T>(
    val id: Int,
    private val response: suspend () -> Response<T>,
    private val onSuccessful: (Response<T>) -> Unit,
    private val onUnsuccessful: (() -> Unit)? = null,
    private val onUnknownHostException: (() -> Unit)? = null,
    private val onSocketTimeoutException: (() -> Unit)? = null,
    private val workCondition: (() -> Boolean)? = null,
    private val workConditionFalseCallback: (() -> Unit)? = null,
    private val throwUnableConnectIfFalse: Boolean = false,
    private val goodCallback: (() -> Unit)? = null,
    val badCallback: (() -> Unit)? = null,
    val retryIfError: Boolean = true) {

    private val networkState: NetworkState by inject(NetworkState::class.java)

    suspend fun invokeQueue(): NetworkQueueState {
        // If null or true, then process network queue.
        return if(workCondition?.invoke() != false) {
            try {
                val response = response()
                if(response.isSuccessful) {
                    onSuccessful(response)
                    goodCallback?.invoke()
                    NetworkQueueState.COMPLETED
                }
                else {
                    onUnsuccessful?.invoke()
                    badCallback?.invoke()
                    NetworkQueueState.UNSUCCESSFUL
                }
            }
            catch(e: UnknownHostException) {
                onUnknownHostException?.invoke()
                networkState.setState(false)
                badCallback?.invoke()
                NetworkQueueState.UNABLE_CONNECT
            }
            catch(e: SocketTimeoutException) {
                onSocketTimeoutException?.invoke()
                NetworkQueueState.TIME_OUT
            }
        }
        else {
            workConditionFalseCallback?.invoke()
            if(throwUnableConnectIfFalse) {
                NetworkQueueState.UNABLE_CONNECT
            }
            else {
                NetworkQueueState.COMPLETED
            }
        }
    }
}