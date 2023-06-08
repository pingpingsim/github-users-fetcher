package com.tawk.githubusers.data.repository

import com.tawk.githubusers.data.local.UserDao
import com.tawk.githubusers.data.remote.UserRemoteDataSource
import com.tawk.githubusers.utils.performGetOperation
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserDao
) {

    fun getUsers() = performGetOperation(
        databaseQuery = { localDataSource.getAllUsers() },
        networkCall = { remoteDataSource.getUsers() },
        saveCallResult = { localDataSource.insertAll(it) }
    )
}