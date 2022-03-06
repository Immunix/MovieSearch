package com.immunix.moviesearch.data.model

data class MovieResult(
    val id: Int,
    val poster_path: String?,
    val release_date: String,
    val original_title: String,
    val title: String
)
