package com.example.storyapp.data.model

data class ServiceResponse <T>(
    val error: Boolean?,
    val message: String?,
    val listStory: List<T>?
)

data class ServiceLoginResponse <T>(
    val error: Boolean?,
    val message: String?,
    val loginResult: T?
)

data class ServiceSuccessResponse(
    val error: Boolean?,
    val message: String?
)
