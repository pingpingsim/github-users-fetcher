package com.tawk.githubusers.ui.details

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tawk.githubusers.R
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.databinding.ActivityDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

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
    }

    fun initToolbar() {
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

    fun loadUserInfo() {
        Glide.with(binding.root)
            .load(user?.avatarUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(binding.imgCover)

        binding.textFollowers.setText(
            getResources().getString(R.string.title_followers, 123)
        )
        binding.textFollowing.setText(
            getResources().getString(R.string.title_following, 89)
        )
    }
}