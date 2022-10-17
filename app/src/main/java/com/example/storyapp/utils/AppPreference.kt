package com.example.storyapp.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreference {
    const val PREF_NAME = "catalog-Preference"

    const val IS_USER_LOGIN = "is_user_login"
    const val USER_TOKEN = "user_token"

    fun setPreference(context: Context): SharedPreferences.Editor{
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
    }

    fun getPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}