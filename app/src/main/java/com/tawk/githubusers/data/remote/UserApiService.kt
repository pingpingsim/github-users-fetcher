package com.tawk.githubusers.data.remote

import com.tawk.githubusers.data.entities.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    @GET("users")
    suspend fun getAllUsers(
        @Query("since") userIdStartIndex: Int?
    ): List<User>

    @GET("users/{username}/followers")
    suspend fun getFollowers(
        @Path("username") username: String,
    ): List<User>

    @GET("users/{username}/following")
    suspend fun getFollowing(
        @Path("username") username: String,
    ): List<User>
}