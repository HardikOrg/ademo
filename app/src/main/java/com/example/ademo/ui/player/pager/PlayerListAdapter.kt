package com.example.ademo.ui.player.pager

import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.ademo.R
import com.example.ademo.databinding.RvPlayerItemBinding
import com.example.ademo.ui.base.BaseRecyclerAdapter
import com.example.ademo.utils.PlayerItem

class PlayerListAdapter(val states: Pair<Int, Int>) :
    BaseRecyclerAdapter<PlayerItem, RvPlayerItemBinding>(RvPlayerItemBinding::inflate) {
    // Without default onclick
    override fun bind(binding: RvPlayerItemBinding, item: PlayerItem, id: Int) {
        val placeholder = CircularProgressDrawable(binding.root.context).apply {
            setColorSchemeColors(ContextCompat.getColor(binding.root.context, R.color.s_pink))
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