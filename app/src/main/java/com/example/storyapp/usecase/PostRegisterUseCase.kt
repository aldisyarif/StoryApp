package com.example.storyapp.usecase

import android.content.Context
import com.example.storyapp.baseclass.BaseUseCase
import com.example.storyapp.data.model.RegisterBody
import com.example.storyapp.data.model.ServiceSuccessResponse
import com.example.storyapp.repository.IStoryRepository
import com.example.storyapp.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PostRegisterUseCase(
    private val repository: IStoryRepository,
    context: Context?
): BaseUseCase(context) {

    operator fun invoke(
        registerBody: RegisterBody
    ): Flow<Resources<ServiceSuccessResponse>>{
        return flow {
            emit(Resources.loading())
            try {
                val response = repository.registerUser(registerBody)
                emit(Resources.success(response))
            } catch (e: Exception){
                emit(Resources.error(e.message ?: "", null))
            }
        }.flowOn(Dispatchers.IO)
    }
}