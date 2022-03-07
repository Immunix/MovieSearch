package com.immunix.moviesearch.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.immunix.moviesearch.data.api.MovieAPI
import com.immunix.moviesearch.data.model.MovieResult
import okio.IOException
import retrofit2.HttpException

private const val MOVIE_STARTING_PAGE_INDEX = 1

class MoviePagingSource(
    private val movieAPI: MovieAPI,
    private val searchQuery: String?
) : PagingSource<Int, MovieResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
        val pageIndex = params.key ?: MOVIE_STARTING_PAGE_INDEX

        return try {
            val movieResponse = movieAPI.getMovies(searchQuery, pageIndex)
            val movies = movieResponse.results

            LoadResult.Page(
                data = movies,
                prevKey = if (pageIndex == MOVIE_STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (movies.isEmpty()) null else pageIndex + 1
            )
        } catch (exception: IOException) {
            Log.e("Paging Exception", "${exception.message}", exception)
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.e("Paging Exception", "${exception.message}", exception)
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResult>): Int? {
        TODO("Not yet implemented")
    }
}