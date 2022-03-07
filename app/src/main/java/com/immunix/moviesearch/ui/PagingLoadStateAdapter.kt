package com.immunix.moviesearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.immunix.moviesearch.databinding.MovieLoadStateFooterBinding

class PagingLoadStateAdapter(private val retryCallback: () -> Unit) :
    LoadStateAdapter<PagingLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(private val binding: MovieLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                retryCallback.invoke()
            }
        }

        fun bindLoadState(loadState: LoadState) = binding.apply {
            progressBar.isVisible = loadState is LoadState.Loading
            buttonRetry.isVisible = loadState !is LoadState.Loading
            errorInfo.isVisible = loadState !is LoadState.Loading

            if (loadState is LoadState.Error) {
                errorInfo.text = loadState.error.localizedMessage
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bindLoadState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            MovieLoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}