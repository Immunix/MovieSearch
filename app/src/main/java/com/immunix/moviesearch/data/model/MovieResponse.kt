package com.immunix.moviesearch.data.model

data class MovieResponse(
    val page: Int,
    val results: List<MovieResult>,
    val total_results: Int,
    val total_pages: Int
)
