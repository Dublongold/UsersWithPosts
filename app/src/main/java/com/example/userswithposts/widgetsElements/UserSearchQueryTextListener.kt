package com.example.userswithposts.widgetsElements

import android.content.Context
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.userswithposts.recyclerViewAdapters.UsersListRecyclerViewAdapter
import com.example.userswithposts.viewModels.UserListViewModel

class UserSearchQueryTextListener(
    private val context: Context?,
    private val adapter: UsersListRecyclerViewAdapter,
    private val viewModel: UserListViewModel
): SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!viewModel.isSearchMode.value) {
            viewModel.setSearchMode(true)
        }
        viewModel.searchUsers(
            query = query,
            onSuccessful = {
                adapter.submitList(it.body()!!.users)
            },
            badCallback = {
                Toast.makeText(context, "Unable to search users.", Toast.LENGTH_SHORT).show()
            })
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText.isNullOrEmpty()) {
            if(viewModel.isSearchMode.value) {
                viewModel.setSearchMode(false)
            }
            adapter.submitList(viewModel.usersList.value)
        }
        return true
    }
}