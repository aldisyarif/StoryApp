package com.example.storyapp.ui.detail

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.baseclass.BaseActivity
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : BaseActivity() {

    private val binding: ActivityDetailStoryBinding by lazy {
        ActivityDetailStoryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val obj = intent.getParcelableExtra<StoryModel>(EXTRA_STORY)
        if (obj != null){
            with(binding){
                tvItemName.text = obj.name
                Glide.with(this@DetailStoryActivity)
                    .load(obj.photoUrl)
                    .into(imgPost)
                tvContentPost.text = obj.description
            }

        }
    }

    companion object {
        const val EXTRA_STORY = "story"
    }
}