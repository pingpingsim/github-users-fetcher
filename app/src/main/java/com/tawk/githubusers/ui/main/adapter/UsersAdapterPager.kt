package com.tawk.githubusers.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tawk.githubusers.R
import com.tawk.githubusers.data.entities.User
import com.tawk.githubusers.databinding.ItemUserBinding

class UsersAdapterPager(private val listener: UserItemListener) :
    PagingDataAdapter<User, UserViewHolder>(REPO_COMPARATOR) {
  //  private val items = ArrayList<User>()

//    fun setItems(items: ArrayList<User>) {
//        this.items.clear()
//        this.items.addAll(items)
//        notifyDataSetChanged()
//    }

    interface UserItemListener {
        fun onClickedUser(userId: Int)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val binding: ItemUserBinding =
//            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return UserViewHolder(binding, listener)
        return UserViewHolder(parent)
    }

//    override fun getItemCount(): Int = items.size
}

class UserViewHolder(
    parent: ViewGroup,
) : RecyclerView.ViewHolder( LayoutInflater.from(parent.context)
    .inflate(R.layout.item_user, parent, false))
   {// View.OnClickListener
       //private val listener: UsersAdapter.UserItemListener
   private val itemBinding = ItemUserBinding.bind(itemView)

    private var user: User? = null

    init {
        //itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: User?) {
        user = item
        itemBinding.username.text = item?.login
        itemBinding.details.text = "details"
        Glide.with(itemBinding.root)
            .load(item?.avatarUrl)
            .transform(CircleCrop())
            .into(itemBinding.avatar)
    }

//    override fun onClick(v: View?) {
//        listener.onClickedUser(user.id)
//    }
}