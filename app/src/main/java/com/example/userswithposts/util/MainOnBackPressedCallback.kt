package com.example.userswithposts.util

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import com.example.userswithposts.R
import com.example.userswithposts.views.PostFragment
import com.example.userswithposts.views.PostsListFragment
import com.example.userswithposts.views.ProfileFragment
import com.example.userswithposts.views.UsersListFragment
import kotlin.system.exitProcess

class MainOnBackPressedCallback(private val supportFragmentManager: FragmentManager): OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        when(val firstFragment = supportFragmentManager.fragments.firstOrNull()) {
            is ProfileFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, UsersListFragment())
                    .commit()
            }
            is PostFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer,
                        if(firstFragment.fromPostsList) {
                            PostsListFragment(firstFragment.userId)
                        }
                        else {
                            ProfileFragment(firstFragment.userId)
                        }
                    )
                    .commit()
            }
            is PostsListFragment -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, ProfileFragment(firstFragment.userId))
                    .commit()
            }
            else -> {
                val activity = firstFragment?.activity
                if(activity != null && !activity.isDestroyed && !activity.isFinishing) {
                    activity.finish()
                }
            }
        }
    }
}