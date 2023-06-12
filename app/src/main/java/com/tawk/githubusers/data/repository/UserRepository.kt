package com.tawk.githubusers.data.repository

import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.data.remote.UserApiService
import java.util.concurrent.Flow
import javax.inject.Inject

class UserRepository @Inject
constructor(
    private val userApiService: UserApiService,
) {
    suspend fun getFollowers(username: String): List<User> {
        return userApiService.getFollowers(username)
    }

    suspend fun getFollowing(username: String): List<User> {
        return userApiService.getFollowing(username)
    }
}