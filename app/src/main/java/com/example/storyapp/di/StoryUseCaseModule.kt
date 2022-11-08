package com.example.storyapp.di

import com.example.storyapp.repository.IStoryRepository
import com.example.storyapp.usecase.*
import com.example.storyapp.utils.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object StoryUseCaseModule {

    @Provides
    fun provideGetAllStoryUseCase(
        iStoryRepository: IStoryRepository
    ): GetAllStoryUseCase {
        return GetAllStoryUseCase(iStoryRepository, MyApplication.applicationContext)
    }

    @Provides
    fun provideAllMapStoryUseCase(
        iStoryRepository: IStoryRepository
    ): GetAllMapStoryUseCase {
        return GetAllMapStoryUseCase(iStoryRepository, MyApplication.applicationContext)
    }

    @Provides
    fun providePostNewStoryUseCase(
        iStoryRepository: IStoryRepository
    ): PostNewStoryUseCase {
        return PostNewStoryUseCase(iStoryRepository, MyApplication.applicationContext)
    }

    @Provides
    fun providePostLoginUserUseCase(
        iStoryRepository: IStoryRepository
    ): PostLoginUserUseCase {
        return PostLoginUserUseCase(iStoryRepository, MyApplication.applicationContext)
    }

    @Provides
    fun providePostRegisterUseCase(
        iStoryRepository: IStoryRepository
    ): PostRegisterUseCase {
        return PostRegisterUseCase(iStoryRepository, MyApplication.applicationContext)
    }
}