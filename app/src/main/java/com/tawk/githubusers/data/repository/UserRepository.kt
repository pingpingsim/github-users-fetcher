package com.tawk.githubusers.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.data.remote.UserApiService
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class UserRepository @Inject
constructor(
    private val userApiService: UserApiService,
) {
    val usersSuccessLiveData = MutableLiveData<User>()
    val usersFailureLiveData = MutableLiveData<Boolean>()

    /*
    this fun is suspend fun means it will execute in different thread
     */
    suspend fun getUserProfile(username: String) {
        try {
            val response = userApiService.getUserProfile(username)
            Log.d(TAG, "$response")

            if (response.isSuccessful) {
                Log.d(TAG, "SUCCESS")
                Log.d(TAG, "${response.body()}")
                usersSuccessLiveData.postValue(response.body())

            } else {
                Log.d(TAG, "FAILURE")
                Log.d(TAG, "${response.body()}")
                usersFailureLiveData.postValue(true)
            }

        } catch (e: UnknownHostException) {
            e.message?.let { Log.e(TAG, it) }
            //this exception occurs when there is no internet connection or host is not available
            //so inform user that something went wrong
            usersFailureLiveData.postValue(true)
        } catch (e: SocketTimeoutException) {
            e.message?.let { Log.e(TAG, it) }
            //this exception occurs when time out will happen
            //so inform user that something went wrong
            usersFailureLiveData.postValue(true)
        } catch (e: Exception) {
            e.message?.let { Log.e(TAG, it) }
            //this is generic exception handling
            //so inform user that something went wrong
            usersFailureLiveData.postValue(true)
        }

    }

    suspend fun getFollowers(username: String): List<User> {
        return userApiService.getFollowers(username)
    }

    suspend fun getFollowing(username: String): List<User> {
        return userApiService.getFollowing(username)
    }

    companion object {
        val TAG = UserRepository::class.java.simpleName
    }

}