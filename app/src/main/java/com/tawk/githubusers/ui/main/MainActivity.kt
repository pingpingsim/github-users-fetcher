package com.tawk.githubusers.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
        setupObservers()

        networkConnectionManager = NetworkConnectionManager(application)
        networkConnectionManager.observe(this, { isConnected ->
            when (isConnected) {
                true -> {//show indicator
                    binding.rvUsers.visibility = View.VISIBLE
                    binding.emptyDataView.visibility = View.GONE

                    if (adapter.itemCount == 0) {
                        adapter.refresh()
                    }
                }
                false -> {//show indicator
//                    if (adapter.itemCount == 0) {//show empty list view with retry
//                        binding.emptyDataView.visibility = View.VISIBLE
//                        binding.rvUsers.visibility = View.GONE
//                        binding.btnRetry.setOnClickListener(View.OnClickListener { view ->
//                            adapter.refresh()
//                        })
//                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = UsersAdapterPager(this)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter =
            adapter.withLoadStateFooter(footer = UserLoadStateAdapter { adapter.retry() })
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            mainViewModel.getUsers().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onClickedUser(characterId: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        startActivity(intent)
    }
}