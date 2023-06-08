package com.tawk.githubusers.ui.main

import androidx.lifecycle.ViewModel
import com.tawk.githubusers.data.remote.UserRemoteDataSource
import com.tawk.githubusers.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    val users = userRepository.getUsers()


}