package com.tawk.githubusers.di

import com.tawk.githubusers.data.remote.UserRemoteDataSource
import com.tawk.githubusers.data.remote.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

//    @Provides
//    fun provideRemoteDataSource(apiService: UserApiService): UserRemoteDataSource =
//        UserRemoteDataSource(apiService)

}