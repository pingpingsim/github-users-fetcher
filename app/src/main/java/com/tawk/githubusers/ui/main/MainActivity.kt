package com.tawk.githubusers.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tawk.githubusers.R
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.databinding.ActivityMainBinding
import com.tawk.githubusers.ui.details.DetailsActivity
import com.tawk.githubusers.ui.main.adapter.UserLoadStateAdapter
import com.tawk.githubusers.ui.main.adapter.UsersAdapterPager
import com.tawk.githubusers.utils.NetworkConnectionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UsersAdapterPager.UserItemListener {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapterPager
    private lateinit var networkConnectionManager: NetworkConnectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.root)
        setupRecyclerView()
        searchAllUsers()
        setupNetworkConnectionObserver()
        initSearchView()
    }

    private fun setupRecyclerView() {
        adapter = UsersAdapterPager(this)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter =
            adapter.withLoadStateFooter(footer = UserLoadStateAdapter { adapter.retry() })
        adapter.addLoadStateListener { updateUiOnNewLoadSate(it) }
        binding.btnRetry.setOnClickListener(View.OnClickListener { view ->
            adapter.retry()
        })
    }

    private fun updateUiOnNewLoadSate(loadState: CombinedLoadStates) {
        val displayEmptySearch =
            loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount == 0
        binding.emptyDataState.root.isVisible = displayEmptySearch
        binding.rvUsers.isVisible = !displayEmptySearch && (loadState.refresh !is LoadState.Error)
        binding.progressState.isVisible = loadState.refresh is LoadState.Loading
        binding.errorState.isVisible = loadState.refresh is LoadState.Error
    }

    private fun searchAllUsers() {
        lifecycleScope.launch {
            mainViewModel.getAllUsers().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun filterUsers(filter: String) {
        lifecycleScope.launch {
            mainViewModel.getAllFilteredUsers(filter).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    // TODO
    private fun setupNetworkConnectionObserver() {
        networkConnectionManager = NetworkConnectionManager(application)
        networkConnectionManager.observe(this) { isConnected ->
            when (isConnected) {
                true -> {
                    //Snackbar.make(binding.root, resources.getString(R.string.msg_active_connection), Snackbar.LENGTH_SHORT).show()
                }
                false -> {
//                    Snackbar.make(
//                        binding.root,
//                        resources.getString(R.string.msg_offline_mode),
//                        Snackbar.LENGTH_SHORT
//                    ).show()
                }
            }
        }
    }

    private fun initSearchView() {
        binding.searchBar.isFocusable = false;
        binding.searchBar.isIconified = false;
        binding.searchBar.clearFocus();
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!query.isNullOrEmpty()) {
                    binding.rvUsers.scrollToPosition(0)
                    filterUsers(query.trim())
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNullOrEmpty()) {
                    searchAllUsers()
                }
                return false
            }
        })
    }

    override fun onClickedUser(user: User) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}