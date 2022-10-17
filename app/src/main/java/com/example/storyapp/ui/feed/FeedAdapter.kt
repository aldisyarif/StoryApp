package com.example.storyapp.ui.feed

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.databinding.ItemFeedBinding

class FeedAdapter: RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    private var listStory = mutableListOf<StoryModel>()

    private var onItemClickCallback: OnItemClickCallback?= null


    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<StoryModel>){
        notifyDataSetChanged()
        listStory.clear()
        listStory.addAll(list)
    }

    fun setOnClickCallback(callback: OnItemClickCallback){
        this.onItemClickCallback = callback
    }

    inner class ViewHolder(private val binding: ItemFeedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(storyModel: StoryModel) {
            with(binding){
                tvItemName.text = storyModel.name
                Glide.with(binding.root)
                    .load(storyModel.photoUrl)
                    .into(imgPost)
                tvContentPost.text = storyModel.description

                itemView.setOnClickListener {
                    onItemClickCallback?.onClickCallback(storyModel)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    interface OnItemClickCallback {
        fun onClickCallback(storyModel: StoryModel)
    }


}