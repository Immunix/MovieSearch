package com.immunix.moviesearch.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.immunix.moviesearch.data.MoviePagingSource
import com.immunix.moviesearch.data.api.MovieAPI
import javax.inject.Inject

class MovieRepo @Inject constructor(
    private val movieAPI: MovieAPI
) {
    fun getMovieData(searchQuery: String?) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 60,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieAPI, searchQuery)
            }
        ).liveData
}