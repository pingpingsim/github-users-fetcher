package com.tawk.githubusers.data.remote

import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApiService: UserApiService
) : BaseDataSource() {
    suspend fun getUsers() = getResult { userApiService.getAllUsers(0) }
}