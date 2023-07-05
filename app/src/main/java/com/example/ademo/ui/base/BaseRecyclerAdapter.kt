package com.example.ademo.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


// BaseAdapter for 1 type of data
open class BaseRecyclerAdapter<DataType : Any, VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) :
    RecyclerView.Adapter<BaseRecyclerAdapter<DataType, VB>.ViewHolder>() {
    private var dataList = listOf<DataType>()
    open var onClick: ((DataType, Int) -> Unit)? = null

    // Do not forget to super() while overriding
    open fun bind(binding: VB, item: DataType, id: Int) {
        onClick?.let { click ->
            binding.root.setOnClickListener {
                click.invoke(item, id)
            }
        }
    }

    inner class ViewHolder(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bindVH(item: DataType, id: Int) {
            bind(binding, item, id)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(bindingInflater(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        viewHolder.bindVH(dataList[position], position)

    override fun getItemCount() = dataList.size

    fun setData(list: List<DataType>) {
        dataList = list
        notifyDataSetChanged()
    }

    fun addData(item: DataType) {
        dataList = dataList + item
        notifyDataSetChanged()
    }

    fun addData(list: List<DataType>) {
        dataList = dataList + list
        notifyDataSetChanged()
    }

    fun clearData() {
        dataList = listOf()
        notifyDataSetChanged()
    }
}