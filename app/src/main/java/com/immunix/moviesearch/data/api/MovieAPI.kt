package com.immunix.moviesearch.data.api

import com.immunix.moviesearch.BuildConfig
import com.immunix.moviesearch.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    companion object {
        const val MOVIE_KEY = BuildConfig.MOVIE_KEY
    }

    @GET("search/movie?api_key=$MOVIE_KEY")
    suspend fun getMovies(
        @Query("query") query: String?,
        @Query("page") page: Int
    ): MovieResponse

}