package com.example.userswithposts.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.userswithposts.R
import com.example.userswithposts.viewModels.PostViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PostFragment(val userId: Int, private val postId: Int = -1, val fromPostsList: Boolean = false): Fragment() {
    private val viewModel: PostViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setPostId(postId)

        viewModel.networkState.watchIsAvailableForConnectionLostIcon(
            lifecycleOwner = viewLifecycleOwner,
            view = view
        )

        watchPost()
        watchAuthor()

        viewModel.configureViewNetworkObservers(viewLifecycleOwner)

        viewModel.loadPost {
            if(!isDetached) {
                view.findViewById<ProgressBar>(R.id.loadingPostProgressBar).visibility = View.GONE
            }
        }
        viewModel.loadAuthor()

        configureGoBackButton()
    }

    private fun watchPost() {
        view?.let {view ->
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentPost.collect {
                        if (it != null) {
                            val postTitle =  view.findViewById<TextView>(R.id.postTitle)
                            val postTags = view.findViewById<TextView>(R.id.postTags)
                            val postContentContainer = view.findViewById<ScrollView>(R.id.postContentContainer)

                            if(postTags.visibility == View.INVISIBLE
                                || postTitle.visibility == View.INVISIBLE
                                || postContentContainer.visibility == View.INVISIBLE
                            ) {
                                postTags.visibility = View.VISIBLE
                                postTitle.visibility = View.VISIBLE
                                postContentContainer.visibility = View.VISIBLE
                            }

                            postTitle.text = it.title
                            view.findViewById<TextView>(R.id.postBody).text = it.body
                            postTags.text =
                                getString(R.string.post_tags, it.tags.joinToString())
                            view.findViewById<TextView>(R.id.postReactions).text =
                                getString(R.string.post_reactions, it.reactions)
                            if (viewModel.postAuthor.value == null) {
                                view.findViewById<TextView>(R.id.postAuthor).text =
                                    getString(R.string.post_author, "Loading...", "")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun watchAuthor() {
        view?.let {view ->
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.postAuthor.collect {
                        if(it != null) {
                            view.findViewById<TextView>(R.id.postAuthor).text = getString(
                                R.string.post_author,
                                it.username,
                                getString(R.string.post_author_id, it.id))
                        }
                    }
                }
            }
        }
    }
    private fun configureGoBackButton() {
        view?.let { view ->
            view.findViewById<ImageButton>(R.id.goOutFromPost).run {
                setOnClickListener {
                    if(fromPostsList) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.mainContainer, PostsListFragment(userId))
                            .commit()
                    }
                    else {
                        checkIfUserIdIsNullAndChangeFragment(viewModel.postAuthor.value?.id)
                    }
                }
            }
        }
    }

    private fun checkIfUserIdIsNullAndChangeFragment(userId: Int?) {
        if(userId != null) {
            viewModel.clearNetworkQueue()
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, ProfileFragment(userId))
                .commit()
        }
        else {
            viewModel.clearNetworkQueue()
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, UsersListFragment())
                .commit()
        }
    }
}