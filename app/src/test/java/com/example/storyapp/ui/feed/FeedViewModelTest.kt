package com.example.storyapp.ui.feed

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.usecase.GetAllStoryUseCase
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.PagedTestSource
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class FeedViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()

    @Mock
    private lateinit var getAllStoryUseCase: GetAllStoryUseCase

    @Mock
    private lateinit var app: Application

    private lateinit var feedViewModel: FeedViewModel
    private val dummyStory = DataDummy.generateDummyStoryEntity()


    @Before
    fun setUp(){
        app = mock(Application::class.java)
        getAllStoryUseCase = mock(GetAllStoryUseCase::class.java)
        feedViewModel = FeedViewModel(getAllStoryUseCase, app)
    }

    @Test
    fun `when get story list should not null and success`() = runTest {
        val data: PagingData<StoryModel> = PagedTestSource.snapshot(dummyStory)
        val expectedDataStories = MutableLiveData(data)
        Mockito.`when`(getAllStoryUseCase.invoke()).thenReturn(flowOf(expectedDataStories.value!!))
        feedViewModel.getAllStory()

        val result = feedViewModel.allStoryLiveData.getOrAwaitValue()?.data

        val differ = AsyncPagingDataDiffer(
            diffCallback = FeedAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        result?.let {
            differ.submitData(it)
        }
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}