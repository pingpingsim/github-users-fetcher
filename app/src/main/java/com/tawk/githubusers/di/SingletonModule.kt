package com.tawk.githubusers.di

import android.content.Context
import com.tawk.githubusers.data.local.UserDatabase
import com.tawk.githubusers.data.remote.UserApiService
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
    fun provideDatabase(@ApplicationContext appContext: Context) =
        UserDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideUserDao(db: UserDatabase) = db.userDao()

    @Provides
    fun provideUserRepository(userApiService: UserApiService): UserRepository =
        UserRepository(userApiService)

}