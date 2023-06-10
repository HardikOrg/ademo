package com.example.ademo.ui.slushe.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.ademo.databinding.FragmentSlusheDetailedBinding
import com.example.ademo.ui.MainActivity
import com.example.ademo.ui.base.BaseFragment

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

        // Initial from List
        binding.apply {
            author.text = item.author
            title.text = item.title
        }

        viewModel.details.observe(viewLifecycleOwner) {
            Glide.with(view).load(it.bigImageSrc).placeholder(binding.bigImage.drawable)
                .into(binding.bigImage)
            Glide.with(view).load(it.authorImageSrc).placeholder(binding.authorImg.drawable)
                .into(binding.authorImg)

            binding.apply {
                author.text = it.authorName
                position.text = it.authorPosition
                title.text = it.title

                detailBlock.viewField.text = it.stats[0]
                detailBlock.favField.text = it.stats[1]
                detailBlock.likeField.text = it.stats[2]
                detailBlock.comField.text = it.stats[3]
            }
        }

        binding.toolbar.apply {
            setNavigationOnClickListener {
                (requireActivity() as MainActivity).onSupportNavigateUp()
            }
        }
    }
}
