package com.example.storyapp.usecase

import android.content.Context
import com.example.storyapp.baseclass.BaseUseCase
import com.example.storyapp.data.model.ServiceSuccessResponse
import com.example.storyapp.repository.IStoryRepository
import com.example.storyapp.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostNewStoryUseCase(
    private val repository: IStoryRepository,
    context: Context?
): BaseUseCase(context) {

    operator fun invoke(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Resources<ServiceSuccessResponse>> {

        return flow {
            emit(Resources.loading())
            try {
                val response = repository.postNewStory(
                    bearerToken,
                    file,
                    description,
                    lat,
                    lon
                )
                emit(Resources.success(response))

            } catch (e: Exception) {
                emit(Resources.error(e.message ?: "", null))
            }

        }.flowOn(Dispatchers.IO)
    }
}