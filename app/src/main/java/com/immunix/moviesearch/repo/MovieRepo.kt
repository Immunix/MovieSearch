package com.immunix.moviesearch.repo

import com.immunix.moviesearch.data.api.MovieAPI
import javax.inject.Inject

class MovieRepo @Inject constructor(
    private val movieAPI: MovieAPI
) {
    suspend fun getMovieData(query: String) = movieAPI.getMovies(query)
}