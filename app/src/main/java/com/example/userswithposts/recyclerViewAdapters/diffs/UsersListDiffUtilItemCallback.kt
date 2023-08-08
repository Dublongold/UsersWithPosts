package com.example.userswithposts.recyclerViewAdapters.diffs

import androidx.recyclerview.widget.DiffUtil
import com.example.userswithposts.models.User

class UsersListDiffUtilItemCallback: DiffUtil.ItemCallback<User?>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}