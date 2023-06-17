package com.tawk.githubusers.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.data.local.UserDatabase
import com.tawk.githubusers.data.remote.UserApiService
import com.tawk.githubusers.data.repository.UserRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

const val PAGE_SIZE = 20
const val FILTER_PAGE_SIZE = 1000

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: UserApiService,
    private val database: UserDatabase,
) :
    ViewModel() {
//    private val queryFlow = MutableStateFlow("")
//
//    fun onQueryChanged(query: String) {
//        queryFlow.value = query
//    }

    /**
     * A PagingSource still loads the data; but when the paged data is exhausted, the Paging library triggers the RemoteMediator to load new data from the network source.
     * The RemoteMediator stores the new data in the local database, so an in-memory cache in the ViewModel is unnecessary.
     * Finally, the PagingSource invalidates itself, and the Pager creates a new instance to load the fresh data from the database.
     */

    @OptIn(ExperimentalPagingApi::class)
    fun getAllUsers(): Flow<PagingData<User>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                //prefetchDistance = 10,
                initialLoadSize = PAGE_SIZE,
            ),
            pagingSourceFactory = {
                database.userDao().getAllUsersPagingSource()
            },
            remoteMediator = UserRemoteMediator(
                apiService,
                database,
            )
        ).flow.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    fun getAllFilteredUsers(query: String): Flow<PagingData<User>> =
        Pager(
            config = PagingConfig(
                pageSize = FILTER_PAGE_SIZE,
                initialLoadSize = FILTER_PAGE_SIZE,
            ),
            pagingSourceFactory = {
                database.userDao()
                    .getFilteredUsersPagingSource(query)
            },
        ).flow.cachedIn(viewModelScope)
}
