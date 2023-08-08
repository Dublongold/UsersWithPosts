package com.example.userswithposts.util

import android.content.res.Configuration
import androidx.fragment.app.Fragment

val Fragment.isDarkTheme: Boolean
    get() = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
