package com.immunix.moviesearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.immunix.moviesearch.BuildConfig
import com.immunix.moviesearch.R
import com.immunix.moviesearch.data.model.MovieResult
import com.immunix.moviesearch.databinding.RecyclerMovieCellBinding

class MovieAdapter(private val listener: OnMovieClickListener) :
    PagingDataAdapter<MovieResult, MovieAdapter.MovieHolder>(MovieComparator) {

    inner class MovieHolder(private val binding: RecyclerMovieCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener.onMovieClick(getItem(bindingAdapterPosition)?.id!!)
                }
            }

            binding.btnMore.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    listener.onMoreClick(getItem(bindingAdapterPosition)!!)
                }
            }
        }

        fun bindMovie(movie: MovieResult) = binding.apply {
            if (movie.posterPath.isNullOrEmpty()) {
                movieImage.load(R.drawable.ic_broken_image)
            } else {
                movieImage.load(BuildConfig.IMAGE_URL + movie.posterPath) {
                    crossfade(true)
                    error(R.drawable.ic_broken_image)
                    placeholder(R.drawable.ic_broken_image)
                }
            }

            movieTitle.text = movie.title
            movieDetails.text = movie.overview
        }
    }

    object MovieComparator : DiffUtil.ItemCallback<MovieResult>() {
        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = getItem(position)

        if (movie != null) {
            holder.bindMovie(movie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(
            RecyclerMovieCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    interface OnMovieClickListener {
        fun onMovieClick(movieId: Int)
        fun onMoreClick(movie: MovieResult)
    }
}