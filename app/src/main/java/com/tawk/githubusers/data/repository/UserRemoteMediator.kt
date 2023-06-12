package com.tawk.githubusers.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator;
import androidx.room.withTransaction
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.data.local.UserDatabase
import com.tawk.githubusers.data.remote.UserApiService
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val apiService: UserApiService,
    private val database: UserDatabase,
) : RemoteMediator<Int, User>() {
    val userDao = database.userDao()

    override suspend fun initialize(): InitializeAction {
        val dbUserCount: Int = userDao.getCount()
        return when (dbUserCount){
            0 -> InitializeAction.LAUNCH_INITIAL_REFRESH
            else -> return InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    lastItem.id
                }
            }

            Log.d(TAG, "Load Type= " + loadType.toString() + " Key= " + loadKey.toString())
            val userList = apiService.getAllUsers(loadKey)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.clearAll()
                }
                userDao.insertAll(userList)
            }

            MediatorResult.Success(
                endOfPaginationReached = (userList == null || userList.isEmpty())
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}