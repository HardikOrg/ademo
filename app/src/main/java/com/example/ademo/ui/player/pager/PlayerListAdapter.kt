package com.example.ademo.ui.player.pager

import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.ademo.R
import com.example.ademo.databinding.PlayerItemBinding
import com.example.ademo.ui.base.BaseRecyclerAdapter
import com.example.ademo.utils.PlayerItem

class PlayerListAdapter(val states: Pair<Int, Int>) :
    BaseRecyclerAdapter<PlayerItem, PlayerItemBinding>(PlayerItemBinding::inflate) {
    // Without default onclick
    override fun bind(binding: PlayerItemBinding, item: PlayerItem, id: Int) {
        val placeholder = CircularProgressDrawable(binding.root.context).apply {
            setColorSchemeColors(binding.root.context.resources.getColor(R.color.s_pink))
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        Glide.with(binding.root)
            .load(item.preview)
            .placeholder(placeholder)
            .centerCrop()
            .into(binding.image)

        binding.imageButton.apply {
            setImageResource(if (item.isLiked) states.second else states.first)
            setOnClickListener {
                onClick?.invoke(item, id)
                setImageResource(if (item.isLiked) states.second else states.first)
            }
        }
    }
}