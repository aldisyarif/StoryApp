package com.example.storyapp.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.model.ServiceResponse
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.usecase.GetAllMapStoryUseCase
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.Resources
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
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
class MapViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()

    @Mock
    private lateinit var getAllMapStoryUseCase: GetAllMapStoryUseCase

    private lateinit var mapViewModel: MapViewModel
    private val dummyStory = DataDummy.generateDummyStoryWithMap()

    @Before
    fun setUp(){
        getAllMapStoryUseCase = mock(GetAllMapStoryUseCase::class.java)
        mapViewModel = MapViewModel(getAllMapStoryUseCase)
    }

    @Test
    fun `when stories with location successfully`() = runTest {
        val expectedDataStories = MutableLiveData<Resources<ServiceResponse<StoryModel>>>()
        expectedDataStories.value = Resources.success(dummyStory)

        expectedDataStories.value?.let {
            Mockito.`when`(getAllMapStoryUseCase.invoke(1)).thenReturn(flowOf(it))
        }
        mapViewModel.getAllMapStory(1)

        val result = mapViewModel.allStoryLiveData.getOrAwaitValue()

        assertNotNull(result)
        assertEquals(dummyStory.listStory?.size, result?.size)

    }

    @Test
    fun `when stories with location failed`() = runTest {
        val expectedDataStories = MutableLiveData<Resources<ServiceResponse<StoryModel>>>()
        expectedDataStories.value = Resources.error("failed", dummyStory)

        Mockito.`when`(getAllMapStoryUseCase.invoke(1)).thenReturn(flowOf(expectedDataStories.value!!))
        mapViewModel.getAllMapStory(1)

        val result = mapViewModel.errorResult.getOrAwaitValue()

        assertNotNull(result)
        assertEquals("failed", result)

    }

}