package com.immunix.moviesearch.data.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val page: Int,
    val results: List<MovieResult>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
