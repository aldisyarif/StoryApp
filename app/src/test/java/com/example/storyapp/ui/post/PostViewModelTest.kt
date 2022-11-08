package com.example.storyapp.ui.post

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.model.ServiceSuccessResponse
import com.example.storyapp.usecase.PostNewStoryUseCase
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.Resources
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PostViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()

    @Mock
    private lateinit var postNewStoryUseCase: PostNewStoryUseCase


    private lateinit var postViewModel: PostViewModel
    private val dummyFile = DataDummy.multipartFileDummy()
    private val dummyDescription = DataDummy.dataRequestBodyDummy()
    private val dummyDataUploadSuccessResponse = DataDummy.dataFileUploadResponseDummy()
    private val dummyDataUploadFailedResponse = DataDummy.dataFileUploadResponseFailedDummy()

    @Before
    fun setUp(){
        postNewStoryUseCase = mock(PostNewStoryUseCase::class.java)
        postViewModel = PostViewModel(postNewStoryUseCase)
    }

    @Test
    fun `Upload new story successfully`() = runTest {
        val dataUploadResponse = MutableLiveData<Resources<ServiceSuccessResponse>>()
        dataUploadResponse.value = Resources.success(dummyDataUploadSuccessResponse)

        Mockito.`when`(postNewStoryUseCase.invoke(
            dummyFile,
            dummyDescription,
            null,
            null
        )).thenReturn(flowOf(dataUploadResponse.value!!))

        postViewModel.addNewStory(
            dummyFile,
            dummyDescription,
            null,
            null
        )

        val result = postViewModel.postResult.getOrAwaitValue()

        assertNotNull(result)
        assertTrue(result?.error == false)

    }

    @Test
    fun `Upload new story failed`() = runTest {
        val dataUploadResponse = MutableLiveData<Resources<ServiceSuccessResponse>>()
        dataUploadResponse.value = Resources.error("Add Story Failed", dummyDataUploadFailedResponse)

        Mockito.`when`(postNewStoryUseCase.invoke(
            dummyFile,
            dummyDescription,
            null,
            null
        )).thenReturn(flowOf(dataUploadResponse.value!!))

        postViewModel.addNewStory(
            dummyFile,
            dummyDescription,
            null,
            null
        )

        val result = postViewModel.errorResult.getOrAwaitValue()

        assertNotNull(result)
        assertTrue(result == "Add Story Failed")

    }

}