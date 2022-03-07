package com.immunix.moviesearch.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import com.immunix.moviesearch.R
import com.immunix.moviesearch.databinding.FragmentMovieBinding
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

        setPagingAdapter()

        movieViewModel.movies.observe(viewLifecycleOwner) { pagingData ->
            movieAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
        movieViewModel.searchMovies("AS")

        observeLoadState()

        binding.buttonRetry.setOnClickListener {
            movieAdapter.retry()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setPagingAdapter() = binding.apply {
        movieAdapter = MovieAdapter(this@MovieFragment)
        movieRecycler.adapter = movieAdapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter {
                movieAdapter.retry()
            },
            footer = PagingLoadStateAdapter {
                movieAdapter.retry()
            }
        )
    }

    private fun observeLoadState() = binding.apply {
        movieAdapter.addLoadStateListener { loadState ->
            progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            movieRecycler.isVisible = loadState.source.refresh is LoadState.NotLoading
            dataInfo.isVisible = loadState.source.refresh is LoadState.Error
            buttonRetry.isVisible = loadState.source.refresh is LoadState.Error

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                movieAdapter.itemCount < 1
            ) {
                movieRecycler.isVisible = false
                dataInfo.isVisible = true
                    .also {
                        dataInfo.text = getString(R.string.no_results)
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