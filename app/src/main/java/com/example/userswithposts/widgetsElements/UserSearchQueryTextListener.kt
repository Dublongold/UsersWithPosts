package com.example.userswithposts.widgetsElements

import android.content.Context
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.userswithposts.recyclerViewAdapters.UsersListRecyclerViewAdapter
import com.example.userswithposts.viewModels.UserListViewModel

/**
 * This is necessary for searching users on query.
 */
class UserSearchQueryTextListener(
    /** Context is required for Toast. If internet connection is lost, then it will show text "Unable to search users."*/
    private val context: Context?,
    /** Required to display found users.*/
    private val adapter: UsersListRecyclerViewAdapter,
    /** Required to set search mode and searching users.*/
    private val viewModel: UserListViewModel
): SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        // If we aren't in search mode then set search mode to true.
        if(!viewModel.isSearchMode.value) {
            viewModel.setSearchMode(true)
        }
        // Make network request for search users.
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
        // We can leave search mode only if the text is null or empty.
        if(newText.isNullOrEmpty()) {
            if(viewModel.isSearchMode.value) {
                viewModel.setSearchMode(false)
            }
            adapter.submitList(viewModel.usersList.value)
        }
        return true
    }
}