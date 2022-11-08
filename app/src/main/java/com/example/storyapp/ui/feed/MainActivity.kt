package com.example.storyapp.ui.feed

import android.content.Intent
import android.os.Bundle
import com.example.storyapp.baseclass.BaseActivity
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.authentication.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (!isLoggedIn) {
            startNavigationToLogin()
            finish()
        }
    }

    private fun startNavigationToLogin() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val IS_FROM_POST = "is_from_post"
    }

}