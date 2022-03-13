package com.immunix.moviesearch.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.immunix.moviesearch.R
import androidx.navigation.fragment.navArgs
import coil.load
import com.immunix.moviesearch.BuildConfig
import com.immunix.moviesearch.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val navArgs: MovieDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailsBinding.bind(view)

        binding.apply {
            if (navArgs.movie.posterPath.isNullOrEmpty()) {
                movieImage.load(R.drawable.ic_broken_image)
            } else {
                movieImage.load(BuildConfig.IMAGE_URL + navArgs.movie.posterPath) {
                    crossfade(true)
                    error(R.drawable.ic_broken_image)
                    placeholder(R.drawable.ic_broken_image)
                }
            }

            movieOverview.text = "Overview:\n\n${navArgs.movie.overview}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}