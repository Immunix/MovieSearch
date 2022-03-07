package com.immunix.moviesearch.data.model

import java.io.Serializable

data class MovieResult(
    val id: Int,
    val poster_path: String?,
    val release_date: String,
    val original_title: String,
    val title: String,
    val vote_count: Int,
    val overview: String
) : Serializable
