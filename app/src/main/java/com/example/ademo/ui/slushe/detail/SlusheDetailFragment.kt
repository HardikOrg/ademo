package com.example.ademo.ui.slushe.detail

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ademo.R
import com.example.ademo.databinding.FragmentSlusheDetailedBinding
import com.example.ademo.databinding.RvTagItemBinding
import com.example.ademo.ui.MainActivity
import com.example.ademo.ui.base.BaseFragment
import com.example.ademo.ui.base.BaseRecyclerAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager

class SlusheDetailFragment :
    BaseFragment<FragmentSlusheDetailedBinding>(FragmentSlusheDetailedBinding::inflate) {
    // ViewModel is scoped to the fragment -> die after going back to the list
    private val args: SlusheDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<SlusheDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = args.pageItem
        viewModel.setPageItem(item)

        Glide.with(view)
            .load(item.imgLink)
            .into(binding.bigImage)

        Glide.with(view)
            .load(item.authorImgLink)
            .into(binding.authorImg)

        // Initial data from List
        binding.apply {
            author.text = item.author
            title.text = item.title
        }

        binding.loadingRecyclerView.recyclerView.apply {
            layoutManager = FlexboxLayoutManager(context, FlexDirection.ROW).apply {
                addItemDecoration(FlexboxItemDecoration(context).apply {
                    setDrawable(ContextCompat.getDrawable(context, R.drawable.rv_item_separator))
                    setOrientation(FlexboxItemDecoration.BOTH)
                })
            }
            adapter = SlusheTagsAdapter()
        }

        viewModel.details.observe(viewLifecycleOwner) {
            binding.apply {
                Glide.with(view).load(it.bigImageSrc).placeholder(bigImage.drawable)
                    .into(bigImage)
                Glide.with(view).load(it.authorImageSrc).placeholder(authorImg.drawable)
                    .into(authorImg)

                author.text = it.authorName
                position.text = it.authorPosition
                title.text = it.title

                detailBlock.viewField.text = it.stats[0]
                detailBlock.favField.text = it.stats[1]
                detailBlock.likeField.text = it.stats[2]
                detailBlock.comField.text = it.stats[3]

                (loadingRecyclerView.recyclerView.adapter as SlusheTagsAdapter).setData(it.tags)
                loadingRecyclerView.setLoading(false)
            }
        }

        binding.toolbar.apply {
            setNavigationOnClickListener {
                (requireActivity() as MainActivity).onSupportNavigateUp()
            }
        }
    }

    inner class SlusheTagsAdapter :
        BaseRecyclerAdapter<String, RvTagItemBinding>(RvTagItemBinding::inflate) {
        override fun bind(binding: RvTagItemBinding, item: String, id: Int) {
            binding.text.text = item
        }
    }
}
