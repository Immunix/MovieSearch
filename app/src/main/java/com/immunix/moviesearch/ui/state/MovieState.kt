package com.immunix.moviesearch.ui.state

import com.immunix.moviesearch.data.model.MovieResponse

sealed class MovieState {
    object Loading : MovieState()
    data class Success(val movieData: MovieResponse) : MovieState()
    data class Error(val errorMsg: String) : MovieState()
}
