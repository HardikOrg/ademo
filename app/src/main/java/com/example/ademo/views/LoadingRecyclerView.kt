package com.example.ademo.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.ademo.databinding.LayoutLoadingRecyclerBinding

class LoadingRecyclerView : FrameLayout {
    constructor(context: Context) :
            this(context, null)

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    val binding = LayoutLoadingRecyclerBinding.inflate(LayoutInflater.from(context)).also {
        addView(it.root)
    }

    val recyclerView = binding.recylerView

    fun setLoading(isLoading: Boolean) {
        binding.loadingImage.apply {
            if (isLoading) this.visibility = VISIBLE else this.visibility = GONE
        }
    }
}