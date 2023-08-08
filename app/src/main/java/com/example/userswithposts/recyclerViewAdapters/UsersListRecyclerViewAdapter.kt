package com.example.userswithposts.recyclerViewAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.userswithposts.R
import com.example.userswithposts.models.User
import com.example.userswithposts.recyclerViewAdapters.diffs.UsersListDiffUtilItemCallback
import com.example.userswithposts.recyclerViewAdapters.viewHolders.UsersListViewHolder
import com.squareup.picasso.Picasso

class UsersListRecyclerViewAdapter: ListAdapter<User?, UsersListViewHolder>(UsersListDiffUtilItemCallback()) {
    var goToProfileCallback: ((Int) -> Unit)? = null
    var loadMorePostsCallback: (() -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListViewHolder {
        return UsersListViewHolder(
            if (viewType == 0) {
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.users_list_load_more, parent, false)
            }
            else {
                LayoutInflater.from(parent.context).inflate(R.layout.users_list_item, parent, false)
            }
        )
    }

    override fun getItemViewType(position: Int) = if(currentList[position] == null) 0 else 1

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: UsersListViewHolder, position: Int) {
        val item = currentList[position]

        holder.run {
            if(item != null) {
                item.image?.let {
                    Picasso.get()
                        .load(it)
                        .into(userImage)
                }
                userFirstName?.text = item.firstName
                userLastName?.text = item.lastName
                userEmail?.text = item.email
                userAge?.text = itemView.context.resources.getString(R.string.user_age_in_brackets, item.age)
                itemView.setOnClickListener {
                    Log.i("Click on user", "${item.username ?: "(Username is null!!!)"} (id: ${item.id}) clicked!")
                    goToProfileCallback?.invoke(item.id)
                }
            }
            else {
                loadMoreUsersButton?.setOnClickListener {
                    Log.i("Load more products", "Try to load more products...")
                    loadMorePostsCallback?.invoke()
                }
            }
        }
    }
}