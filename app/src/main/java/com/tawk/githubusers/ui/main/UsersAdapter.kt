package com.tawk.githubusers.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.databinding.ItemUserBinding

class UsersAdapter(private val listener: UserItemListener) :
    RecyclerView.Adapter<UserViewHolder>() {

    interface UserItemListener {
        fun onClickedUser(characterId: Int)
    }

    private val items = ArrayList<User>()

    fun setItems(items: ArrayList<User>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding: ItemUserBinding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(items[position])
}

class UserViewHolder(
    private val itemBinding: ItemUserBinding,
    private val listener: UsersAdapter.UserItemListener
) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var character: User

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: User) {
        this.character = item
        itemBinding.username.text = item.login
        itemBinding.details.text = "details"
        Glide.with(itemBinding.root)
            .load(item.avatarUrl)
            .transform(CircleCrop())
            .into(itemBinding.avatar)
    }

    override fun onClick(v: View?) {
        listener.onClickedUser(character.id)
    }
}