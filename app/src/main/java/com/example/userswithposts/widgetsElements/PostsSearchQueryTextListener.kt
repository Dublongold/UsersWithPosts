package com.example.userswithposts.widgetsElements

import android.content.Context
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.example.userswithposts.recyclerViewAdapters.PostsListRecyclerViewAdapter
import com.example.userswithposts.viewModels.PostsListViewModel

class PostsSearchQueryTextListener(
    private val context: Context?,
    private val adapter: PostsListRecyclerViewAdapter,
    private val viewModel: PostsListViewModel,
    private val userId: Int
): SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!viewModel.isSearchMode.value) {
            viewModel.setSearchMode(true)
        }
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
        if(newText.isNullOrEmpty()) {
            if(viewModel.isSearchMode.value) {
                viewModel.setSearchMode(false)
            }
            adapter.submitList(viewModel.postsList.value)
        }
        return true
    }
}