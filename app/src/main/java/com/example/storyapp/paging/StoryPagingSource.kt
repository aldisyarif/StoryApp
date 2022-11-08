package com.example.storyapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.repository.IStoryRepository
import retrofit2.HttpException

class StoryPagingSource(
    private val repository: IStoryRepository,
    private val bearerToken: String?
): PagingSource<Int, StoryModel>() {


    override fun getRefreshKey(state: PagingState<Int, StoryModel>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(anchorPosition = it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryModel> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = repository.getAllStories(
                bearerToken,
                page = position,
                size = params.loadSize
            ).listStory ?: listOf()

            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )

        } catch (exception: Exception){
            return LoadResult.Error(exception)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}