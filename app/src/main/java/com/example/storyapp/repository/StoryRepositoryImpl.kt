package com.example.storyapp.repository

import com.example.storyapp.data.model.*
import com.example.storyapp.data.remote.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepositoryImpl(
    private val service: ApiService
): IStoryRepository {

    override suspend fun getAllStories(
        authorization: String?,
        page: Int?,
        size: Int?
    ): ServiceResponse<StoryModel> =
        service.getAllStories(
            authorization,
            page,
            size
        )

    override suspend fun getAllMapStories(
        authorization: String?,
        location: Int
    ): ServiceResponse<StoryModel> =
        service.getAllMapStories(
            authorization,
            location
        )

    override suspend fun postNewStory(
        authorization: String?,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): ServiceSuccessResponse =
        service.addNewStory(
            authorization, file, description, lat, lon
        )

    override suspend fun loginUser(
        loginBody: LoginBody
    ): ServiceLoginResponse<UserModel> =
        service.login(loginBody)

    override suspend fun registerUser(
        registerBody: RegisterBody
    ): ServiceSuccessResponse =
        service.register(registerBody)


}