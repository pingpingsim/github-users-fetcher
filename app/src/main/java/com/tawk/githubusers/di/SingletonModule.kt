package com.tawk.githubusers.di

import android.content.Context
import com.tawk.githubusers.data.local.AppDatabase
import com.tawk.githubusers.data.local.UserDao
import com.tawk.githubusers.data.remote.UserApiService
import com.tawk.githubusers.data.remote.UserRemoteDataSource
import com.tawk.githubusers.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): UserApiService = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(UserApiService::class.java)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(userApiService: UserApiService) =
        UserRemoteDataSource(userApiService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: UserRemoteDataSource,
        localDataSource: UserDao
    ) =
        UserRepository(remoteDataSource, localDataSource)
}