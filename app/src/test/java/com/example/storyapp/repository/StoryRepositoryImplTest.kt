package com.example.storyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.data.remote.ApiService
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.FakeApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StoryRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: ApiService
    private lateinit var repository: StoryRepositoryImpl


    @Before
    fun setUp(){
        service = FakeApiService()
        repository = StoryRepositoryImpl(service)
    }

    @Test
    fun `when Stores Response Not Null`() = runTest {
        val expectedStory = DataDummy.generateStoryFakeResponse()
        val actualStory = repository.getAllStories("api keys",1, 10)
        assertNotNull(actualStory)
        assertEquals(expectedStory.listStory?.size, actualStory.listStory?.size)
    }

    @Test
    fun `when Map Story Response Not Null`() = runTest {
        val expectedStory = DataDummy.generateStoryFakeResponse()
        val actualStoryMap = repository.getAllMapStories("api keys",1)
        assertNotNull(actualStoryMap)
        assertEquals(expectedStory.listStory?.size, actualStoryMap.listStory?.size)
    }

    @Test
    fun `when posting Story Response success`() = runTest {
        val expectedStory = DataDummy.dataFileUploadResponseDummy()
        val resultPostStory = repository.postNewStory(
            "api keys",
            DataDummy.multipartFileDummy(),
            DataDummy.dataRequestBodyDummy(),
            null,
            null
        )
        assertNotNull(resultPostStory)
        assertEquals(expectedStory.error, resultPostStory.error)
    }

    @Test
    fun `when Login Response Success`() = runTest {
        val expectedStory = DataDummy.generateLoginFakeResponse()
        val actualResultLogin = repository.loginUser(
            DataDummy.generateLoginUser()
        )
        assertNotNull(actualResultLogin)
        assertEquals(expectedStory.error, actualResultLogin.error)
    }

    @Test
    fun `when Register Response Success`() = runTest {
        val expectedStory = DataDummy.generateRegisterFakeResponse()
        val actualResultRegister = repository.registerUser(
            DataDummy.generateRegisterUser()
        )
        assertNotNull(actualResultRegister)
        assertEquals(expectedStory.error, actualResultRegister.error)
    }
}