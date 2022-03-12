package com.immunix.moviesearch.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.immunix.moviesearch.BuildConfig
import com.immunix.moviesearch.R
import com.immunix.moviesearch.data.model.MovieResult
import com.immunix.moviesearch.databinding.FragmentMovieBinding
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

        //TODO add a check for no data searched yet || add default search
        observeLoadState()

        binding.buttonRetry.setOnClickListener {
            movieAdapter.retry()
        }

        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    binding.movieRecycler.scrollToPosition(0)
                    movieViewModel.searchMovies(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
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

            // search failed to return any matching results
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

            val error = when {
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }

            if (error != null) {
                dataInfo.text = error.error.localizedMessage
            }
        }
    }

    private fun openWebPage(movieId: Int) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.MOVIE_URL + movieId))

        try {
            startActivity(webIntent)
        } catch (e: ActivityNotFoundException) {
            Log.i("movieId", "${e.message}")
        }
    }

    override fun onMovieClick(movieId: Int) {
        openWebPage(movieId)
    }

    override fun onMoreClick(movie: MovieResult) {
        val action =
            MovieFragmentDirections.actionMovieFragmentToMovieDetailsFragment(movie, movie.title)
        findNavController().navigate(action)
    }
}