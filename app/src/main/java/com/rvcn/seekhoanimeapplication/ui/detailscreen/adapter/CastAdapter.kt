package com.rvcn.seekhoanimeapplication.ui.detailscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rvcn.seekhoanimeapplication.databinding.ItemCastBinding
import com.rvcn.seekhoanimeapplication.domain.model.Cast

class CastAdapter :
    ListAdapter<Cast, CastAdapter.ViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(a: Cast, b: Cast) = a.name == b.name
        override fun areContentsTheSame(a: Cast, b: Cast) = a == b
    }

    override fun onCreateViewHolder(p: ViewGroup, v: Int): ViewHolder {
        val b = ItemCastBinding.inflate(
            LayoutInflater.from(p.context), p, false
        )
        return ViewHolder(b)
    }

    override fun onBindViewHolder(h: ViewHolder, pos: Int) = h.bind(getItem(pos))

    class ViewHolder(private val b: ItemCastBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun bind(cast: Cast) {
            b.tvCastName.text = cast.name
            Glide.with(b.ivCast)
                .load(cast.imageUrl)
                .into(b.ivCast)
        }
    }
}
