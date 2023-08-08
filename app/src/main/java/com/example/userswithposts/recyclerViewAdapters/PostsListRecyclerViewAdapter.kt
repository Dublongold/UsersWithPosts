package com.example.userswithposts.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.userswithposts.R
import com.example.userswithposts.models.Post
import com.example.userswithposts.recyclerViewAdapters.diffs.PostsListDiffUtilItemCallback
import com.example.userswithposts.recyclerViewAdapters.viewHolders.PostsListViewHolder

class PostsListRecyclerViewAdapter(private val stringsForItems: Map<String, String>): ListAdapter<Post?, PostsListViewHolder>(
    PostsListDiffUtilItemCallback()
) {
    var goToPostCallback: ((Int) -> Unit)? = null
    var loadMorePostsCallback: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsListViewHolder {
        return PostsListViewHolder(
            if (viewType == 0) {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.posts_list_load_more, parent, false)
            }
            else {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.posts_list_item, parent, false)
            }
        )
    }

    override fun getItemViewType(position: Int) = if(currentList[position] == null) 0 else 1

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: PostsListViewHolder, position: Int) {
        val item = currentList[position]

        holder.run {
            if(item != null) {
                postPreviewTitle?.text = item.title
                postPreviewTags?.text = stringsForItems.getOrDefault("tag", "Tags: %s").format(item.tags.joinToString())
                postPreviewReactions?.text = stringsForItems.getOrDefault("reactions", "Reactions: %s").format(item.reactions)
                postPreviewUserOrPostId?.text = stringsForItems.getOrDefault("id", "Id: ").format(item.id)
                itemView.setOnClickListener {
                    Log.i("Click on post", "Post with id ${item.id} was clicked!")
                    goToPostCallback?.invoke(item.id)
                }
            }
            else {
                loadMorePostsButton?.setOnClickListener {
                    Log.i("Load more products", "Try to load more products...")
                    loadMorePostsCallback?.invoke()
                }
            }
        }
    }
}