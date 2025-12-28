package com.rvcn.seekhoanimeapplication.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rvcn.seekhoanimeapplication.databinding.ItemCarouselBinding
import com.rvcn.seekhoanimeapplication.domain.model.Anime

class CarouselAdapter(private val onItemClick: (Int) -> Unit): ListAdapter<Anime, CarouselAdapter.ViewHolder>(Diff) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarouselAdapter.ViewHolder {
        val binding = ItemCarouselBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position),onItemClick)
    }

    object Diff : DiffUtil.ItemCallback<Anime>(){
        override fun areItemsTheSame(
            oldItem: Anime,
            newItem: Anime
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Anime,
            newItem: Anime
        ): Boolean {
            return oldItem == newItem
        }

    }


    class ViewHolder(private val binding: ItemCarouselBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(anime: Anime, onItemClick: (Int) -> Unit){
            Glide.with(binding.ivPoster)
                .load(anime.imageUrl)
                .centerCrop()
                .into(binding.ivPoster)

            binding.tvTitle.text = anime.title
            binding.tvMeta.text =
                "⭐ ${anime.rating ?: "--"} • ${anime.episodes ?: "--"} eps"

            binding.root.setOnClickListener { onItemClick(anime.id) }
        }

    }
}