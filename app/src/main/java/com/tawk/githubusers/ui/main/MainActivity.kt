package com.tawk.githubusers.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tawk.githubusers.databinding.ActivityMainBinding
import com.tawk.githubusers.ui.main.adapter.UserLoadStateAdapter
import com.tawk.githubusers.ui.main.adapter.UsersAdapterPager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UsersAdapterPager.UserItemListener {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapterPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.root)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = UsersAdapterPager(this)
        binding.charactersRv.layoutManager = LinearLayoutManager(this)
        binding.charactersRv.adapter =
            adapter.withLoadStateFooter(footer = UserLoadStateAdapter { adapter.retry() })
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            mainViewModel.getUsers().collectLatest {
                adapter.submitData(it)
            }
        }
        
//        mainViewModel.users.observe(this, Observer {
//            when (it.status) {
//                Resource.Status.SUCCESS -> {
//                    binding.progressBar.visibility = View.GONE
//                    if (!it.data.isNullOrEmpty()) adapter.setItems(ArrayList(it.data))
//                }
//                Resource.Status.ERROR ->
//                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//
//                Resource.Status.LOADING ->
//                    binding.progressBar.visibility = View.VISIBLE
//            }
//        })
    }

    override fun onClickedUser(characterId: Int) {
//        findNavController().navigate(
//            R.id.action_charactersFragment_to_characterDetailFragment,
//            bundleOf("id" to characterId)
//        )
    }
}