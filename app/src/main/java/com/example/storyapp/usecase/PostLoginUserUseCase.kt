package com.example.storyapp.usecase

import android.content.Context
import com.example.storyapp.baseclass.BaseUseCase
import com.example.storyapp.data.model.LoginBody
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.model.ServiceLoginResponse
import com.example.storyapp.repository.IStoryRepository
import com.example.storyapp.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PostLoginUserUseCase(
    private val repository: IStoryRepository,
    context: Context?
    ): BaseUseCase(context) {

        operator fun invoke(loginBody: LoginBody) : Flow<Resources<ServiceLoginResponse<UserModel>>>{
            return flow {
                emit(Resources.loading())
                try {
                    val response = repository.loginUser(loginBody)
                    emit(Resources.success(response))

                }catch (e: Exception) {
                    emit(Resources.error(e.localizedMessage ?: "", null))
                }

            }.flowOn(Dispatchers.IO)
        }

}