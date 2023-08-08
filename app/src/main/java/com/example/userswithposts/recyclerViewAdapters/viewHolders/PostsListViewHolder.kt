package com.example.userswithposts.recyclerViewAdapters.viewHolders

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userswithposts.R

class PostsListViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val postPreviewTitle: TextView? = view.findViewById(R.id.postPreviewTitle)
    val postPreviewTags: TextView? = view.findViewById(R.id.postPreviewTags)
    val postPreviewUserOrPostId: TextView? = view.findViewById(R.id.postPreviewUserOrPostId)
    val postPreviewReactions: TextView? = view.findViewById(R.id.postPreviewReactions)
    var loadMorePostsButton: Button? = view.findViewById(R.id.loadMorePostsButton)
}