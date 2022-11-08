package com.example.storyapp.ui.feed

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.model.LocationModel
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.databinding.ItemFeedBinding
import com.example.storyapp.utils.DateFormatter
import java.io.IOException
import java.util.*

class FeedAdapter(
    private val context: Context
): PagingDataAdapter<StoryModel, FeedAdapter.ViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null
    private var onItemClickMapCallback: OnItemClickCallback? = null


    fun setOnClickCallback(callback: OnItemClickCallback,){
        this.onItemClickCallback = callback
        this.onItemClickMapCallback = callback
    }

    inner class ViewHolder(private val binding: ItemFeedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(storyModel: StoryModel) {
            with(binding){
                tvItemName.text = storyModel.name
                Glide.with(binding.root)
                    .load(storyModel.photoUrl)
                    .into(imgPost)
                tvContentPost.text = storyModel.description ?: ""
                tvCreatedAt.text = DateFormatter.formatDate(storyModel.createdAt ?: "", TimeZone.getDefault().id)

                val address = getAddressName(storyModel.lat, storyModel.lon)
                if (!address.isNullOrEmpty()){
                    btnMap.visibility = View.VISIBLE
                    btnMap.text = address
                        btnMap.setOnClickListener {
                            onItemClickMapCallback?.onClickMapCallback(LocationModel(storyModel.name, storyModel.lat, storyModel.lon))
                        }
                } else {
                    btnMap.visibility = View.GONE
                }

                itemView.setOnClickListener {
                    onItemClickCallback?.onClickCallback(storyModel)
                }
            }
        }

        private fun getAddressName(lat: Double?, lon: Double?): String? {
            var addressName: String? = null
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val list = lat?.let {
                    lon?.let { it1 ->
                        geocoder.getFromLocation(it, it1, 1)
                    }
                }
                if (list != null && list.size != 0){
                    addressName = list[0].locality
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return addressName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }


    interface OnItemClickCallback {
        fun onClickCallback(storyModel: StoryModel)
        fun onClickMapCallback(location: LocationModel)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}