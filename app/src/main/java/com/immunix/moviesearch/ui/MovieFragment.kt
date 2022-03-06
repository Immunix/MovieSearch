package com.immunix.moviesearch.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.immunix.moviesearch.R
import com.immunix.moviesearch.databinding.FragmentMovieBinding
import com.immunix.moviesearch.ui.state.MovieState
import com.immunix.moviesearch.utils.Constants.MOVIE_URL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnMovieClickListener {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieViewModel by activityViewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieBinding.bind(view)

        movieAdapter = MovieAdapter(this)
        observeMovieData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeMovieData() = binding.apply {
        movieViewModel.movieData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MovieState.Success -> {
                    progressBar.visibility = View.GONE
                    movieRecycler.visibility = View.VISIBLE
                    movieAdapter.movieList = state.movieData.results
                    movieRecycler.adapter = movieAdapter
                }
                is MovieState.Error -> {
                    progressBar.visibility = View.GONE
                    movieRecycler.visibility = View.GONE
                    dataInfo.text = state.errorMsg
                }
                MovieState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun openWebPage(movieId: Int) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(MOVIE_URL + movieId))

        try {
            startActivity(webIntent)
        } catch (e: ActivityNotFoundException) {
            Log.i("movieId", "${e.message}")
        }
    }

    override fun onMovieClick(movieId: Int) {
        openWebPage(movieId)
    }
}