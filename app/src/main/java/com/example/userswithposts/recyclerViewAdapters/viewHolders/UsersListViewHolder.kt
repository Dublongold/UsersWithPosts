package com.example.userswithposts.recyclerViewAdapters.viewHolders

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.userswithposts.R

class UsersListViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val userImage: ImageView? = view.findViewById(R.id.userImage)
    val userFirstName: TextView? = view.findViewById(R.id.userFirstName)
    val userLastName: TextView? = view.findViewById(R.id.userLastName)
    val userAge: TextView? = view.findViewById(R.id.userAge)
    val userEmail: TextView? = view.findViewById(R.id.userEmail)
    val loadMoreUsersButton: Button? = view.findViewById(R.id.loadMoreUsersButton)
}