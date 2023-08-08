package com.example.userswithposts.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.userswithposts.R
import com.example.userswithposts.models.Post
import com.example.userswithposts.viewModels.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ProfileFragment(private val userId: Int): Fragment() {
    private val viewModel: ProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.networkState.watchIsAvailableForConnectionLostIcon(
            lifecycleOwner = viewLifecycleOwner,
            view = view
        )

        watchUser()

        watchUsersPosts()

        viewModel.configureViewNetworkObservers(viewLifecycleOwner)

        configureGoToUsersButton()

        viewModel.loadUserById(
            id = userId,
            goodCallback = {
                if(!isDetached) {
                    view.findViewById<ProgressBar>(R.id.userProfileLoading).visibility = View.GONE
                }
            })

        viewModel.loadUserPosts(
            goodCallback = {
                val postTitleText = try {
                    getString(R.string.user_profile_posts, viewModel.usersPosts.value.size)
                }
                catch(e: IllegalStateException) {
                    "Error ${e.message}"
                }
                view.findViewById<LinearLayout>(R.id.userProfilePostsContainer).visibility =
                    View.VISIBLE
                view.findViewById<TextView>(R.id.userProfilePostTitle).text = postTitleText

            }
        )

    }

    private fun watchUser() {
        view?.let { view ->
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentUser.collect {
                        if (it != null) {
                            view.findViewById<View>(R.id.userProfileContainer).visibility =
                                View.VISIBLE
                            view.findViewById<TextView>(R.id.userProfileFirstLastMaidenName).text =
                                getString(
                                    R.string.user_first_last_maiden_name,
                                    it.firstName,
                                    it.lastName,
                                    it.maidenName
                                )
                            view.findViewById<TextView>(R.id.userProfileBirthDateData).text =
                                it.birthDate
                            view.findViewById<TextView>(R.id.userProfileGenderData).text = it.gender
                            view.findViewById<TextView>(R.id.userProfileEmailData).text = it.email
                            view.findViewById<TextView>(R.id.userProfilePhoneData).text = it.phone
                            view.findViewById<TextView>(R.id.userProfileAddressData).text =
                                it.address?.address
                            Picasso.get()
                                .load(it.image)
                                .into(view.findViewById<ImageView>(R.id.userProfileImage))
                        }
                    }
                }
            }
        }
    }

    private fun watchUsersPosts() {
        view?.let { view ->
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.usersPosts.collect {
                        val views: List<ConstraintLayout> = listOf(
                            view.findViewById(R.id.userProfileFirstPost),
                            view.findViewById(R.id.userProfileSecondPost),
                            view.findViewById(R.id.userProfileThirdPost)
                        )
                        for ((i, e) in it.withIndex()) {
                            if (i > 3) break
                            if (i == 3) {
                                view.findViewById<Button>(R.id.userProfileSeeMorePosts).run {
                                    visibility = View.VISIBLE
                                    setOnClickListener {
                                    viewModel.clearNetworkQueue()
                                        parentFragmentManager.beginTransaction()
                                            .replace(R.id.mainContainer, PostsListFragment(userId))
                                            .commit()
                                    }
                                }
                                continue
                            }
                            setDataForPost(views[i], e)
                        }
                    }
                }
            }
        }
    }

    private fun setDataForPost(view: View, post: Post) {
        view.visibility = View.VISIBLE
        view.findViewById<TextView>(R.id.postPreviewTitle).text = post.title
        view.findViewById<TextView>(R.id.postPreviewTags).text = getString(R.string.post_tags, post.tags.joinToString())
        view.findViewById<TextView>(R.id.postPreviewUserOrPostId).text = getString(R.string.post_id, post.id)
        view.findViewById<TextView>(R.id.postPreviewReactions).text = getString(R.string.post_reactions, post.reactions)

        view.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, PostFragment(userId, post.id))
                .commit()
        }
    }

    private fun configureGoToUsersButton() {
        view?.let { view ->
            view.findViewById<ImageButton>(R.id.goToUsers).run {
                setOnClickListener {
                    viewModel.clearNetworkQueue()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.mainContainer, UsersListFragment())
                        .commit()
                }
            }
        }
    }
}