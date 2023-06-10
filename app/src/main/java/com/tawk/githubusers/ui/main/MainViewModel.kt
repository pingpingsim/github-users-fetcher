package com.tawk.githubusers.ui.main

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.data.local.UserDatabase
import com.tawk.githubusers.data.remote.UserApiService
import com.tawk.githubusers.data.repository.UserRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val PAGE_SIZE = 20

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: UserApiService,
    private val database: UserDatabase,
) :
    ViewModel() {

    /**
     * A PagingSource still loads the data; but when the paged data is exhausted, the Paging library triggers the RemoteMediator to load new data from the network source.
     * The RemoteMediator stores the new data in the local database, so an in-memory cache in the ViewModel is unnecessary.
     * Finally, the PagingSource invalidates itself, and the Pager creates a new instance to load the fresh data from the database.
     */

    @OptIn(ExperimentalPagingApi::class)
    fun getUsers(): Flow<PagingData<User>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 10,
                initialLoadSize = PAGE_SIZE, // How many items you want to load initially
            ),
            pagingSourceFactory = {
                // The pagingSourceFactory lambda should always return a brand new PagingSource
                // when invoked as PagingSource instances are not reusable.
                database.userDao().getAllUsersPagingSource()
            },
            remoteMediator = UserRemoteMediator(
                apiService,
                database,
            )
        ).flow

}