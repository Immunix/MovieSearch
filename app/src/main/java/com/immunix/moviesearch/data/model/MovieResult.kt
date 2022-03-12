package com.immunix.moviesearch.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MovieResult(
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val title: String,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("genre_ids")
    val genreList: List<Int>,
    val popularity: Float,
    val overview: String
) : Serializable
