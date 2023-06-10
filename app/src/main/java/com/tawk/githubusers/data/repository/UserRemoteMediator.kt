package com.tawk.githubusers.data.repository

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

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val apiService: UserApiService,
    private val database: UserDatabase,
) : RemoteMediator<Int, User>() {
    val userDao = database.userDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): MediatorResult {
        return try {
            // The network load method takes an optional after=<user.id>
            // parameter. For every page after the first, pass the last user
            // ID to let it continue from where it left off. For REFRESH,
            // pass null to load the first page.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    Log.d("TAWK-LOG", "APPEND")
                    val lastItem = state.lastItemOrNull()

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.
                    if (lastItem == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    lastItem.id
                }
            }

            // Suspending network load via Retrofit. This doesn't need to be
            // wrapped in a withContext(Dispatcher.IO) { ... } block since
            // Retrofit's Coroutine CallAdapter dispatches on a worker
            // thread.
            Log.d("TAWK-LOG", loadType.toString() + " " + loadKey.toString())
            val userList = apiService.getAllUsers(loadKey)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDao.clearAll()
                }

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                userDao.insertAll(userList)
            }

            MediatorResult.Success(
                endOfPaginationReached = (userList.isEmpty())
            )
        } catch (e: IOException) {
            Log.d("TAWK-LOG", e.message ?: "IOException")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.d("TAWK-LOG", e.message ?: "HttpException")
            MediatorResult.Error(e)
        }
    }
}