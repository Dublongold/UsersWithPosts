package com.example.userswithposts.recyclerViewAdapters.diffs

import androidx.recyclerview.widget.DiffUtil
import com.example.userswithposts.models.Post

class PostsListDiffUtilItemCallback: DiffUtil.ItemCallback<Post?>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}