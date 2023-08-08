package com.example.userswithposts.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userswithposts.R
import com.example.userswithposts.recyclerViewAdapters.PostsListRecyclerViewAdapter
import com.example.userswithposts.viewModels.PostsListViewModel
import com.example.userswithposts.widgetsElements.PostsSearchQueryTextListener
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PostsListFragment(val userId: Int): Fragment() {

    private val viewModel: PostsListViewModel by inject()
    private lateinit var postPreviewStrings: Map<String, String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        postPreviewStrings = mapOf(
            "tag" to getString(R.string.post_tags),
            "id" to getString(R.string.post_id),
            "reactions" to getString(R.string.post_reactions)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.posts_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userId = userId

        val postsListAdapter = PostsListRecyclerViewAdapter(postPreviewStrings).apply {
            goToPostCallback = {
                viewModel.clearNetworkQueue()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, PostFragment(userId = userId, postId = it, fromPostsList = true))
                    .commit()
            }
            loadMorePostsCallback = {
                if(viewModel.networkState.isAvailable.value) {
                    viewModel.loadMorePosts()
                }
            }
        }

        val postsList = view.findViewById<RecyclerView>(R.id.postsList).apply {
            adapter = postsListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        view.findViewById<ImageButton>(R.id.goToProfile).setOnClickListener {
            viewModel.clearNetworkQueue()
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, ProfileFragment(userId))
                .commit()
        }

        viewModel.networkState.watchIsAvailableForConnectionLostIcon(
            lifecycleOwner = viewLifecycleOwner,
            view = view
        )

        watchUsersList(postsListAdapter)

        viewModel.configureViewNetworkObservers(viewLifecycleOwner)

        viewModel.loadPosts (
            goodCallback = {
                if(!isDetached) {
                    postsList.visibility = View.VISIBLE
                    view.findViewById<ProgressBar>(R.id.loadingPosts).visibility = View.GONE
                }
            }
        )

        watchSearchMode()
        view.findViewById<SearchView>(R.id.searchPosts).setOnQueryTextListener(
            PostsSearchQueryTextListener(context, postsListAdapter, viewModel, userId)
        )
    }

    private fun watchUsersList(adapter: PostsListRecyclerViewAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.postsList.collect {
                    if(!viewModel.isSearchMode.value) {
                        adapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun watchSearchMode() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isSearchMode.collect {
                    view?.run {
                        val titleText = findViewById<TextView>(R.id.postsListTitleText)
                        titleText.text = if (it) {
                            getString(R.string.posts_found_list_title)
                        } else {
                            getString(R.string.posts_list_title)
                        }
                    }
                }
            }
        }
    }
}