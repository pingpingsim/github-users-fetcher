package com.tawk.githubusers.data.remote

import com.tawk.githubusers.data.entities.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {
    @GET("users")
    suspend fun getAllUsers(
        @Query("since") since: Int
    ): Response<List<User>>
}