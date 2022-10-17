package com.example.storyapp.data.remote

import com.example.storyapp.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") authorization: String?,
    ): ServiceResponse<StoryModel>

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") authorization: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): ServiceSuccessResponse

    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): ServiceLoginResponse<UserModel>

    @POST("register")
    suspend fun register(
        @Body registerBody: RegisterBody
    ): ServiceSuccessResponse
}