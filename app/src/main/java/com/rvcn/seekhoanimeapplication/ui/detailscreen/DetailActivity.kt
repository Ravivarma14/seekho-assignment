package com.rvcn.seekhoanimeapplication.ui.detailscreen

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.rvcn.seekhoanimeapplication.R
import com.rvcn.seekhoanimeapplication.databinding.ActivityDetailBinding
import com.rvcn.seekhoanimeapplication.domain.model.Anime
import com.rvcn.seekhoanimeapplication.domain.model.AnimeDetail
import com.rvcn.seekhoanimeapplication.ui.detailscreen.adapter.CastAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ANIME_ID = "extra_anime_id"
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val castAdapter = CastAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCast()

        val animeId = intent.getIntExtra(EXTRA_ANIME_ID, -1)
        Log.d("TAG", "recieved: id: $animeId")
        if (animeId == -1) {
            finish()
            return
        }

        observeState()
        viewModel.loadDetail(animeId)
    }

    private fun setupToolbar() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun setupCast() {
        binding.rvCast.apply {
            layoutManager =
                LinearLayoutManager(this@DetailActivity, RecyclerView.HORIZONTAL, false)
            adapter = castAdapter
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    DetailUiState.Loading -> showLoading()
                    is DetailUiState.Success -> showData(state)
                    is DetailUiState.Error -> showError(state.message)
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBarDetail.visibility = View.VISIBLE
    }

    private fun showError(msg: String) {
        binding.progressBarDetail.visibility = View.GONE
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }

    private fun showData(state: DetailUiState.Success) {
        binding.progressBarDetail.visibility = View.GONE
        bindDetail(state.detail, state.isOffline)
    }

    private fun bindDetail(detail: AnimeDetail, isOffline: Boolean) {

        // Trailer
        if (detail.trailerYoutubeId != null && !isOffline) {
            binding.cardTrailer.visibility = View.VISIBLE
            binding.cvPoster.visibility = View.GONE

            // Extract YouTube video ID from URL
            detail.trailerYoutubeId?.let { url ->
                val videoId = Uri.parse(url).getQueryParameter("v")
//                val videoId = url
                videoId?.let {
                    binding.youtubePlayer.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(it, 0f)
                        }
                    })
                }

            }


        } else {
            binding.cardTrailer.visibility = View.GONE
            binding.cvPoster.visibility = View.VISIBLE

            val fallbackImage =
                detail.trailerThumbnailUrl ?: detail.imageUrl

            Glide.with(this)
                .load(fallbackImage)
                .into(binding.ivPoster)

            if (isOffline) {
                Snackbar.make(
                    binding.root,
                    "Trailer not available offline",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        // Title & meta
        binding.tvTitle.text = detail.title

        binding.tvMeta.text =
            "⭐ ${detail.rating ?: "--"} • ${detail.episodes ?: "--"} eps"

        // Synopsis
        if (detail.synopsis.isNullOrBlank()) {
            binding.tvSynopsis.visibility = View.GONE
        } else {
            binding.tvSynopsis.visibility = View.VISIBLE
            binding.tvSynopsis.text = detail.synopsis
        }

        // Genres
        binding.genreContainer.removeAllViews()
        detail.genres.forEach {
            val chip = LayoutInflater.from(this)
                .inflate(R.layout.item_genre_chip, binding.genreContainer, false) as TextView
            chip.text = it
            binding.genreContainer.addView(chip)
        }

        // Cast (THIS FIXES YOUR CRASH EFFECT)
        if (detail.cast.isEmpty()) {
            binding.rvCast.visibility = View.GONE
        } else {
            binding.rvCast.visibility = View.VISIBLE
            castAdapter.submitList(detail.cast)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayer.release()
    }

}
