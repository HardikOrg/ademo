package com.example.ademo.ui.slushe.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.ademo.R
import com.example.ademo.databinding.ImageItemBinding
import com.example.ademo.utils.PageItem

class SlusheGalleryAdapter(val onClick: (PageItem) -> Unit) :
    RecyclerView.Adapter<SlusheGalleryAdapter.ViewHolder>() {
    var dataSet = listOf<PageItem>()

    class ViewHolder(
        private val binding: ImageItemBinding,
        private val onClick: (PageItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PageItem) {
            binding.text.text = item.title

            val placeholder = CircularProgressDrawable(binding.root.context).apply {
                setColorSchemeColors(binding.root.context.resources.getColor(R.color.s_pink))
                strokeWidth = 5f
                centerRadius = 30f
                start()
            }

            Glide.with(binding.root)
                .load(item.imgLink)
                .placeholder(placeholder)
                .centerCrop()
                .into(binding.image)

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ImageItemBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bind(dataSet[position])

    override fun getItemCount() = dataSet.size

    fun setData(list: List<PageItem>) {
        dataSet = list
        notifyDataSetChanged()
    }

    fun addData(list: List<PageItem>) {
        val size = dataSet.size
        dataSet = dataSet + list
        notifyDataSetChanged()
    }

    fun clearData() {
        dataSet = mutableListOf()
        notifyDataSetChanged()
    }
}
