package com.tawk.githubusers.data.remote

import retrofit2.http.Query
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApiService: UserApiService
) : BaseDataSource() {
//    suspend fun getUsers(
//        userIdStartIndex: Int
//    ) = getResult { userApiService.getAllUsers(userIdStartIndex) }
}