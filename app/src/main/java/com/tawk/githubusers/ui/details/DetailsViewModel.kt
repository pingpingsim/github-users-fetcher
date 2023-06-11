package com.tawk.githubusers.ui.details

import androidx.lifecycle.ViewModel
import com.tawk.githubusers.data.remote.UserApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val apiService: UserApiService,): ViewModel() {

}