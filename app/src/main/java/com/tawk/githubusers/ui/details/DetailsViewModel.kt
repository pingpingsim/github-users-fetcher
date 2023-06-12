package com.tawk.githubusers.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.data.local.UserDao
import com.tawk.githubusers.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val userDao: UserDao, private val userRepository: UserRepository) : ViewModel() {
    val followerList = MutableLiveData<List<User>>()
    val followingList = MutableLiveData<List<User>>()

    fun updateUserNotes(notes: String, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        userDao.updateUserNotes(notes, id)
    }

    fun getUserFollowers(username: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = userRepository.getFollowers(username)
        followerList.postValue(response)
    }

    fun getUserFollowing(username: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = userRepository.getFollowing(username)
        followingList.postValue(response)

        userRepository.getFollowers(username).flatMap {
            followerList.postValue(listOf(it))
            userRepository.getFollowing(username)}
    }

}