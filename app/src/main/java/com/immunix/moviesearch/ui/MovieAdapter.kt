package com.immunix.moviesearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.immunix.moviesearch.R
import com.immunix.moviesearch.data.model.MovieResult
import com.immunix.moviesearch.databinding.RecyclerMovieCellBinding

class MovieAdapter(private val listener: OnMovieClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    inner class MovieHolder(private val binding: RecyclerMovieCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onMovieClick(movieList[adapterPosition].id)
                }
            }
        }

        fun bindMovie(movie: MovieResult) = binding.apply {
            if (movie.poster_path.isNullOrEmpty()) {
                movieImage.load(R.drawable.ic_broken_image)
            } else {
                movieImage.load(movie.poster_path) {
                    crossfade(true)
                    error(R.drawable.ic_broken_image)
                    placeholder(R.drawable.ic_broken_image)
                }
            }

            movieTitle.text = movie.title
            movieDetails.text = movie.release_date
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<MovieResult>() {
        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var movieList: List<MovieResult>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = movieList[position]

        holder.bindMovie(movie)
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

    override fun getItemCount(): Int = movieList.size

    interface OnMovieClickListener {
        fun onMovieClick(movieId: Int)
    }
}