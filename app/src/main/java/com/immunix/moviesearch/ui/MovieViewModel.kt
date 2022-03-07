package com.immunix.moviesearch.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.immunix.moviesearch.repo.MovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepo: MovieRepo
) : ViewModel() {

    private val _searchQuery: MutableLiveData<String> = MutableLiveData()

    val movies = _searchQuery.switchMap { query ->
        movieRepo.getMovieData(query).cachedIn(viewModelScope)
    }

    fun searchMovies(searchQuery: String) {
        _searchQuery.value = searchQuery
    }
}