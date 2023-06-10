package com.example.ademo.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.ademo.R
import com.example.ademo.databinding.PlayerItemBinding
import com.example.ademo.utils.PlayerItem

class PlayerListAdapter(val states: Pair<Int, Int>, val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<PlayerListAdapter.ViewHolder>() {
    var dataSet = listOf<PlayerItem>()

    inner class ViewHolder(private val binding: PlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PlayerItem, id: Int) {
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
                setImageResource(states.first)
                setOnClickListener {
                    setImageResource(states.second)
                    onClick(id)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlayerItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(dataSet[position], position)

    override fun getItemCount() = dataSet.size

    fun setData(list: List<PlayerItem>) {
        dataSet = list
        notifyDataSetChanged()
    }

    fun addData(item: PlayerItem) {
        dataSet = dataSet + item
        notifyDataSetChanged()
    }

    fun addData(list: List<PlayerItem>) {
        dataSet = dataSet + list
        notifyDataSetChanged()
    }

    fun clearData() {
        dataSet = mutableListOf()
        notifyDataSetChanged()
    }
}
