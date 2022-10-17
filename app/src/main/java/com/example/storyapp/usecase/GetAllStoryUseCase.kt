package com.example.storyapp.usecase

import android.content.Context
import com.example.storyapp.baseclass.BaseUseCase
import com.example.storyapp.data.model.ServiceResponse
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.repository.IStoryRepository
import com.example.storyapp.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetAllStoryUseCase(
    private val repository: IStoryRepository,
    context: Context?
): BaseUseCase(context) {

    operator fun invoke(): Flow<Resources<ServiceResponse<StoryModel>>> {
        return flow {
            emit(Resources.loading())
            try {
                val response = repository.getAllStories(
                    bearerToken,
                )
                emit(Resources.success(response))

            } catch (e: Exception){
                emit(Resources.error(e.message ?: "", null))
            }

        }.flowOn(Dispatchers.IO)
    }

}