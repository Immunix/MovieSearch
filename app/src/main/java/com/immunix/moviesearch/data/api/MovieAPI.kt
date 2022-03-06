package com.immunix.moviesearch.data.api

import com.immunix.moviesearch.data.model.MovieResponse
import com.immunix.moviesearch.utils.KeyContainer.MOVIE_KEY
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MovieAPI {

    @Headers("api_key: $MOVIE_KEY")
    @GET("/search/movie")
    suspend fun getMovies(
        @Query("query") query: String
    ): MovieResponse

}