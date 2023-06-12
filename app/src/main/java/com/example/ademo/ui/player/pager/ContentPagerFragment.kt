package com.example.ademo.ui.player.pager

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ademo.R
import com.example.ademo.databinding.FragmentPagerContentBinding
import com.example.ademo.ui.base.BaseFragment
import com.example.ademo.ui.player.PlayerViewModel
import com.example.ademo.utils.Settings

class ContentPagerFragment :
    BaseFragment<FragmentPagerContentBinding>(FragmentPagerContentBinding::inflate) {
    private val viewModel by navGraphViewModels<PlayerViewModel>(R.id.nav_player_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listAdapter =
            PlayerListAdapter(Pair(R.drawable.icon_add, R.drawable.icon_add_check)).apply {
                onClick = viewModel.addClick
            }

        binding.contentRecycler.apply {
            layoutManager = GridLayoutManager(view.context, Settings.recyclerGridWidth)
            adapter = listAdapter
        }

        viewModel.listContent.observe(viewLifecycleOwner) { listAdapter.setData(it) }
    }
}