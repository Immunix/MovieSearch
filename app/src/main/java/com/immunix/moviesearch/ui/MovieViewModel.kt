package com.immunix.moviesearch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immunix.moviesearch.repo.MovieRepo
import com.immunix.moviesearch.ui.state.MovieState
import com.immunix.moviesearch.utils.HasInternetAccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepo: MovieRepo
) : ViewModel() {

    private val _movieData: MutableLiveData<MovieState> = MutableLiveData()
    val movieData: LiveData<MovieState> get() = _movieData

    init {
        fetchMovies()
    }

    fun fetchMovies() = viewModelScope.launch {
        _movieData.value = MovieState.Loading

        val hasInternet = withContext(Dispatchers.IO) {
            HasInternetAccess.execute()
        }

        if (hasInternet) {
            try {
                val response = movieRepo.getMovieData("Thor")
                _movieData.value = MovieState.Success(response)
            } catch (error: IOException) {
                _movieData.value = MovieState.Error("Couldn't fetch data. ${error.message}")
            }
        } else {
            _movieData.value = MovieState.Error("No internet connection available.")
        }
    }

}