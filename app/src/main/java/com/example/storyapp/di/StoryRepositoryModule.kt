package com.example.storyapp.di

import com.example.storyapp.data.remote.ApiService
import com.example.storyapp.repository.IStoryRepository
import com.example.storyapp.repository.StoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object StoryRepositoryModule {

    @Provides
    fun provideRepository(
        service: ApiService
    ): IStoryRepository = StoryRepositoryImpl(service)
}