package com.example.storyapp.utils

import com.example.storyapp.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun dataRequestBodyDummy(): RequestBody {
        val dummyText = "dummy"
        return dummyText.toRequestBody()
    }

    fun multipartFileDummy(): MultipartBody.Part {
        val dummyText = "dummy"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun dataFileUploadResponseDummy() : ServiceSuccessResponse {
        return ServiceSuccessResponse(
            error = false,
            message = "success upload story"
        )
    }

    fun dataFileUploadResponseFailedDummy() : ServiceSuccessResponse {
        return ServiceSuccessResponse(
            error = true,
            message = "failed upload story"
        )
    }

    fun generateDummyStoryEntity(): List<StoryModel>{
        val items = ArrayList<StoryModel>()
        for (i in 0 until 10){
            val story = StoryModel(
                id = "story-P8YC5yrXPDwpIkwd",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666339459291_fkiUGNQy.jpg",
                createdAt = "2022-10-21T08:04:19.294Z",
                name = "test123",
                description = "Lorem Ipsum",
                lon = -6.2332895,
                lat = 106.793723
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyStoryWithMap(): ServiceResponse<StoryModel> {
        val items = ArrayList<StoryModel>()
        for (i in 0 until 10){
            val story = StoryModel(
                id = "story-P8YC5yrXPDwpIkwd",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666339459291_fkiUGNQy.jpg",
                createdAt = "2022-10-21T08:04:19.294Z",
                name = "test123",
                description = "Lorem Ipsum",
                lon = -6.2332895,
                lat = 106.793723
            )
            items.add(story)
        }
        return ServiceResponse(
            error = false,
            message = "success load data",
            listStory = items
        )
    }

    fun generateStoryFakeResponse(): ServiceResponse<StoryModel> {
        val items = ArrayList<StoryModel>()
        for (i in 0 until 10){
            val story = StoryModel(
                id = "story-P8YC5yrXPDwpIkwd",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666339459291_fkiUGNQy.jpg",
                createdAt = "2022-10-21T08:04:19.294Z",
                name = "test123",
                description = "Lorem Ipsum",
                lon = -6.2332895,
                lat = 106.793723
            )
            items.add(story)
        }
        return ServiceResponse(
            error = false,
            message = "success load data",
            listStory = items
        )
    }

    fun generateLoginFakeResponse(): ServiceLoginResponse<UserModel> {
        return ServiceLoginResponse(
            error = false,
            message = "success",
            loginResult = UserModel(
                userId = "user-yj5pc_LARC_AgK61",
                name = "Arif Faizin",
                token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
            )
        )
    }

    fun generateRegisterFakeResponse(): ServiceSuccessResponse {
        return ServiceSuccessResponse(
            error = false,
            message = "User Created"
        )
    }

    fun generateLoginUser(): LoginBody {
        return LoginBody(
            email = "contoh@contoh.com",
            password = "123456"
        )
    }

    fun generateRegisterUser(): RegisterBody {
        return RegisterBody(
            name = "contoh",
            email = "contoh@contoh.com",
            password = "123456"
        )
    }
}