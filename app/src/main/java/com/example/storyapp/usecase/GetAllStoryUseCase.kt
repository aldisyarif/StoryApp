package com.example.storyapp.usecase

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.storyapp.baseclass.BaseUseCase
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.paging.StoryPagingSource
import com.example.storyapp.repository.IStoryRepository
import kotlinx.coroutines.flow.Flow

class GetAllStoryUseCase(
    private val repository: IStoryRepository,
    context: Context?
): BaseUseCase(context) {

    operator fun invoke(): Flow<PagingData<StoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                StoryPagingSource(
                    repository,
                    bearerToken
                )
            }
        ).flow
    }

}