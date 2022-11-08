package com.example.storyapp.ui.feed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.data.model.LocationModel
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.databinding.FragmentFeedBinding
import com.example.storyapp.enums.RequestStatus
import com.example.storyapp.ui.authentication.AuthActivity
import com.example.storyapp.ui.detail.DetailStoryActivity
import com.example.storyapp.ui.feed.MainActivity.Companion.IS_FROM_POST
import com.example.storyapp.ui.post.PostActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: FeedViewModel by viewModels()

    private lateinit var adapter: FeedAdapter

    private val binding: FragmentFeedBinding by lazy {
        FragmentFeedBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null){
            initView()
            initViewModel()
            observeAllStory()
            observeLogOut()
        }
    }


    override fun onResume() {
        super.onResume()
        initView()
        initViewModel()
        observeAllStory()
        intentToMapIfFromPostStory()
    }

    private fun intentToMapIfFromPostStory() {
        if (activity?.intent?.extras?.getBoolean(IS_FROM_POST) == true){
            val direction = FeedFragmentDirections.actionNavigationFeedToNavigationMapFromPostStory(
                null,
                false
            )
            NavHostFragment.findNavController(this@FeedFragment).navigate(direction)
        }
    }


    private fun initViewModel() {
        viewModel.getAllStory()
    }

    private fun initView() {
        adapter = FeedAdapter(requireContext())
        showListFeed()

        adapter.setOnClickCallback(object : FeedAdapter.OnItemClickCallback {
            override fun onClickCallback(storyModel: StoryModel) {
                val intent = Intent(requireContext(), DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, storyModel)
                startActivity(intent)
            }

            override fun onClickMapCallback(location: LocationModel) {
                val direction = FeedFragmentDirections.actionNavigationFeedToNavigationMap(
                    location,
                    false
                )
                NavHostFragment.findNavController(this@FeedFragment).navigate(direction)
            }


        })

        binding.btnToUploadStory.setOnClickListener {
            val intent = Intent(requireContext(), PostActivity::class.java)
            startActivity(intent)
        }

        binding.btnToFeedMaps.setOnClickListener {
            val direction = FeedFragmentDirections.actionNavigationFeedToNavigationMap(
                null,
                true
            )
            NavHostFragment.findNavController(this@FeedFragment).navigate(direction)
        }

        binding.btnLogout.setOnClickListener {
            Log.d("FeedFragment", "initView: oke")
            viewModel.logOut()
        }
    }

    private fun observeLogOut() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed{
            viewModel.isLogOut.collect {
                if (it){
                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    activity?.finish()
                }
            }
        }
    }

    private fun observeAllStory() {
        viewModel.allStoryLiveData.observe(requireActivity()) {
            when (it?.requestStatus){
                RequestStatus.LOADING -> {}
                RequestStatus.SUCCESS -> {
                    it.data?.let { it1 ->
                        adapter.submitData(lifecycle, it1)
                    }
                }
                RequestStatus.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.loadingLiveData.collect {
                isLoading(it)
            }
        }

    }

    private fun isLoading(state: Boolean) {
        with(binding){
            if (state){
                rvFeed.visibility = View.GONE
                btnToUploadStory.visibility = View.GONE
                loadingState.visibility = View.VISIBLE
            } else {
                rvFeed.visibility = View.VISIBLE
                btnToUploadStory.visibility = View.VISIBLE
                loadingState.visibility = View.GONE
            }

        }
    }

    private fun showListFeed() {
        with(binding){
            rvFeed.layoutManager = LinearLayoutManager(requireContext())
            rvFeed.adapter = adapter.withLoadStateFooter(
                footer = FooterStateAdapter {
                    adapter.retry()

                }
            )
        }
    }


}