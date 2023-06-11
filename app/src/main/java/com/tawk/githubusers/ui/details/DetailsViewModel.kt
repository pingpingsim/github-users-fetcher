package com.tawk.githubusers.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tawk.githubusers.data.local.UserDao
import com.tawk.githubusers.data.remote.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {

    fun updateUserNotes(notes: String, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        userDao.updateUserNotes(notes, id)
    }
}