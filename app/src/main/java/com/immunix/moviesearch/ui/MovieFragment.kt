package com.immunix.moviesearch.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.immunix.moviesearch.R
import com.immunix.moviesearch.databinding.FragmentMovieBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment : Fragment(R.layout.fragment_movie) {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieBinding.bind(view)

        movieViewModel.movieData.observe(viewLifecycleOwner) {
            Log.i("API test", "$it")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}