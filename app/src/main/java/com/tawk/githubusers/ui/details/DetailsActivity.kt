package com.tawk.githubusers.ui.details

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tawk.githubusers.R
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.databinding.ActivityDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val detailsViewModel: DetailsViewModel by viewModels()
    private lateinit var binding: ActivityDetailsBinding
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.root)

        initToolbar()
        loadUserInfo()

        //loadFollowingCount()
        //loadFollowersCount()
    }

    private fun loadFollowingCount() {
        user?.let { detailsViewModel.getUserFollowing(it.login) }

        detailsViewModel.followingList.observe(this) { response ->
            response?.let {
                binding.txtFollowing.text = resources.getString(R.string.title_following, it.size)
            }
        }
    }

    private fun loadFollowersCount() {
        user?.let { detailsViewModel.getUserFollowers(it.login) }

        detailsViewModel.followerList.observe(this) { response ->
            response?.let {
                binding.txtFollowers.text = resources.getString(R.string.title_followers, it.size)
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        } else {
            intent.getParcelableExtra<User>("user")
        }
        binding.toolbar.setTitle(user?.login)
    }

    private fun loadUserInfo() {
        Glide.with(binding.root)
            .load(user?.avatarUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(binding.imgCover)

        binding.txtFollowers.setText(
            getResources().getString(R.string.title_followers, 0)
        )
        binding.txtFollowing.setText(
            getResources().getString(R.string.title_following, 0)
        )

        binding.txtUsername.setText(
            getResources().getString(R.string.title_name, user?.login)
        )

        binding.txtCompany.setText(
            getResources().getString(R.string.title_company, "-")
        )

        binding.txtUrl.setText(
            getResources().getString(R.string.title_blog, user?.url)
        )

        binding.editTextNotes.setText(user?.notes)

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                user?.id?.let {
                    detailsViewModel.updateUserNotes(
                        binding.editTextNotes.text.toString(),
                        it
                    )
                    Toast.makeText(
                        this@DetailsActivity,
                        resources.getString(R.string.msg_save_successful),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}