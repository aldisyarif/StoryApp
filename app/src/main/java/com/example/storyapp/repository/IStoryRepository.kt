package com.example.storyapp.repository

import com.example.storyapp.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IStoryRepository {

    suspend fun getAllStories(
        authorization: String?,
        page: Int?,
        size: Int?
    ) : ServiceResponse<StoryModel>

    suspend fun getAllMapStories(
        authorization: String?,
        location: Int
    ) : ServiceResponse<StoryModel>

    suspend fun postNewStory(
        authorization: String?,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) : ServiceSuccessResponse

    suspend fun loginUser(
        loginBody: LoginBody
    ): ServiceLoginResponse<UserModel>

    suspend fun registerUser(
        registerBody: RegisterBody
    ): ServiceSuccessResponse

}