package com.example.storyapp.utils

import com.example.storyapp.data.model.*
import com.example.storyapp.data.remote.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService: ApiService {

    private val dummyResponse = DataDummy.generateStoryFakeResponse()
    private val dummyDataUploadSuccessResponse = DataDummy.dataFileUploadResponseDummy()
    private val loginSuccessResponse = DataDummy.generateLoginFakeResponse()
    private val registerSuccessResponse = DataDummy.generateRegisterFakeResponse()

    override suspend fun getAllStories(
        authorization: String?,
        page: Int?,
        size: Int?
    ): ServiceResponse<StoryModel> =
        dummyResponse

    override suspend fun getAllMapStories(
        authorization: String?,
        location: Int
    ): ServiceResponse<StoryModel> =
        dummyResponse

    override suspend fun addNewStory(
        authorization: String?,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): ServiceSuccessResponse =
        dummyDataUploadSuccessResponse

    override suspend fun login(loginBody: LoginBody): ServiceLoginResponse<UserModel> =
        loginSuccessResponse

    override suspend fun register(registerBody: RegisterBody): ServiceSuccessResponse =
        registerSuccessResponse
}