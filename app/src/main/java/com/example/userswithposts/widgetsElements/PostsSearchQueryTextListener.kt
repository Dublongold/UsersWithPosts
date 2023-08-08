package com.example.userswithposts.widgetsElements

import android.content.Context
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.userswithposts.recyclerViewAdapters.PostsListRecyclerViewAdapter
import com.example.userswithposts.viewModels.PostsListViewModel

/**
 * This is necessary for searching posts on query.
 */
class PostsSearchQueryTextListener(
    /** Context is required for Toast. If internet connection is lost, then it will show text "Unable to search posts."*/
    private val context: Context?,
    /** Required to display found posts.*/
    private val adapter: PostsListRecyclerViewAdapter,
    /** Required to set search mode and searching posts.*/
    private val viewModel: PostsListViewModel,
    /** Required to pass user id for network request.*/
    private val userId: Int
): SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        // If we aren't in search mode then set search mode to true.
        if(!viewModel.isSearchMode.value) {
            viewModel.setSearchMode(true)
        }
        // Make network request for search posts.
        viewModel.searchPosts(
            query = query,
            onSuccessful = {response ->
                adapter.submitList(response.body()!!.posts.filter {
                    it.userId == userId
                })
            },
            badCallback = {
                Toast.makeText(context, "Unable to search posts.", Toast.LENGTH_SHORT).show()
            })
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // We can leave search mode only if the text is null or empty.
        if(newText.isNullOrEmpty()) {
            if(viewModel.isSearchMode.value) {
                viewModel.setSearchMode(false)
            }
            adapter.submitList(viewModel.postsList.value)
        }
        return true
    }
}