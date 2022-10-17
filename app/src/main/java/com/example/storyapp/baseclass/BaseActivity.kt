package com.example.storyapp.baseclass

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.utils.AppPreference

open class BaseActivity: AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    var isLoggedIn: Boolean = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = this.getSharedPreferences(AppPreference.PREF_NAME, Context.MODE_PRIVATE)
        setLoginStatus()
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        setLoginStatus()
    }

    override fun onPause() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setLoginStatus()
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key){
            AppPreference.IS_USER_LOGIN -> {
                setLoginStatus()
            }
        }
    }

    private fun setLoginStatus() {
        isLoggedIn = AppPreference.getPreference(this)
            .getBoolean(AppPreference.IS_USER_LOGIN, false)
    }
}