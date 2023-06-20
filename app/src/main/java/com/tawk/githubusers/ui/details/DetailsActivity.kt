package com.tawk.githubusers.ui.details

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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

        initUser()
        initToolbar()
        displayUserInfo()

        loadRemoteUserProfile()
    }

    private fun loadRemoteUserProfile() {
        user?.let {
            detailsViewModel.getUserProfile(it.login)
            registerObservers()
        }
    }

    private fun registerObservers() {

        detailsViewModel.usersSuccessLiveData.observe(this, Observer { user ->
            //if it is not null then we will display all users
            user?.let {
                detailsViewModel.saveUserProfile(it)
                binding.txtFollowers.text =
                    resources.getString(R.string.title_followers, it.followers)
                binding.txtFollowing.text =
                    resources.getString(R.string.title_following, it.following)
                binding.txtCompany.text =
                    getResources().getString(R.string.title_company, it.company)
                binding.txtUrl.text =
                    getResources().getString(R.string.title_blog, it.blog)
            }
        })

        detailsViewModel.usersFailureLiveData.observe(this, Observer { isFailed ->
            isFailed?.let {

            }
        })
    }

    private fun initUser() {
        user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        } else {
            intent.getParcelableExtra<User>("user")
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.toolbar.setTitle(user?.login)
    }

    private fun displayUserInfo() {
        Glide.with(binding.root)
            .load(user?.avatarUrl)
            .centerCrop()
            .placeholder(R.drawable.placeholder_image)
            .into(binding.imgCover)

        user?.let {
            binding.txtFollowers.setText(
                getResources().getString(R.string.title_followers, it.followers)
            )
            binding.txtFollowing.setText(
                getResources().getString(R.string.title_following, it.following)
            )
            binding.txtUsername.setText(
                getResources().getString(R.string.title_name, it.login)
            )

            binding.txtCompany.setText(
                getResources().getString(R.string.title_company, it.company)
            )

            binding.txtUrl.setText(
                getResources().getString(R.string.title_blog, it.blog)
            )

            binding.editTextNotes.setText(it.notes)
        }

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