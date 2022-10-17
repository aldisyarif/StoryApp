package com.example.storyapp.baseclass

import android.content.Context
import com.example.storyapp.utils.AppPreference

open class BaseUseCase(val context: Context?) {

    val bearerToken get() ="Bearer " + context?.let {
        AppPreference.getPreference(it)
        .getString(AppPreference.USER_TOKEN, "")
    }


}