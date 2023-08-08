package com.example.userswithposts.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.userswithposts.recyclerViewAdapters.UsersListRecyclerViewAdapter
import com.example.userswithposts.viewModels.UserListViewModel
import com.example.userswithposts.widgetsElements.UserSearchQueryTextListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class UsersListFragment: Fragment() {

    private val viewModel: UserListViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.users_list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usersListAdapter = UsersListRecyclerViewAdapter().apply {
            goToProfileCallback = {
                viewModel.clearNetworkQueue()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, ProfileFragment(it))
                    .commit()
            }
            loadMorePostsCallback = {
                if(viewModel.networkState.isAvailable.value) {
                    viewModel.loadMoreUsers()
                }
            }
        }

        val usersList = view.findViewById<RecyclerView>(R.id.usersList).apply {
            adapter = usersListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.networkState.watchIsAvailableForConnectionLostIcon(
            lifecycleOwner = viewLifecycleOwner,
            view = view
        )

        watchUsersList(usersListAdapter)

        viewModel.configureViewNetworkObservers(viewLifecycleOwner)

        viewModel.loadUsers (
            goodCallback = {
                if(!isDetached) {
                    usersList.visibility = View.VISIBLE
                    view.findViewById<ProgressBar>(R.id.loadingUsers).visibility = View.GONE
                }
            }
        )

        watchSearchMode()
        view.findViewById<SearchView>(R.id.searchUsers).setOnQueryTextListener(
            UserSearchQueryTextListener(context, usersListAdapter, viewModel)
        )
    }

    private fun watchUsersList(adapter: UsersListRecyclerViewAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usersList.collect {
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
                        val titleText = findViewById<TextView>(R.id.usersListTitleText)
                        titleText.text = if (it) {
                            getString(R.string.users_found_list_title)
                        } else {
                            getString(R.string.users_list_title)
                        }
                    }
                }
            }
        }
    }
}