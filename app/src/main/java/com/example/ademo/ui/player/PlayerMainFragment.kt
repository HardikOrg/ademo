package com.example.ademo.ui.player

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ademo.R
import com.example.ademo.databinding.FragmentPlayerMainBinding
import com.example.ademo.ui.MainActivity
import com.example.ademo.ui.base.BaseFragment
import com.example.ademo.ui.player.pager.ContentPagerFragment
import com.example.ademo.ui.player.pager.PlaylistPagerFragment
import com.example.ademo.utils.Settings
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File

class PlayerMainFragment :
    BaseFragment<FragmentPlayerMainBinding>(FragmentPlayerMainBinding::inflate) {
    private val viewModel by navGraphViewModels<PlayerViewModel>(R.id.nav_player_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = listOf(ContentPagerFragment(), PlaylistPagerFragment())
        val names = listOf("Content", "Playlist")

        val viewPagerAdapter = ViewPagerAdapter(fragments)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = names[position]
        }.attach()

        if (viewModel.listContent.value!!.isEmpty()) {
            val file = File(requireContext().filesDir, Settings.fetchedDataFilename)
            if (file.exists()) {
                Log.d("PlayerMain", "Data file exists")
                Log.d("PlayerMain", file.readText())
                viewModel.getDataFromFile(file)
            } else {
                Log.d("PlayerMain", "Data file does not exist")
                viewModel.fetchAndSaveData(file)
            }
        }

        binding.playButton.apply {
            setOnClickListener {
                val action = PlayerMainFragmentDirections.actionNavPlayerMainToPlayerVideoFragment()
                findNavController().navigate(action)
            }
        }

        binding.toolbar.apply {
            setTitle(R.string.player_title)
            setNavigationOnClickListener {
                (requireActivity() as MainActivity).onSupportNavigateUp()
            }
        }
    }

    inner class ViewPagerAdapter(private val fragments: List<Fragment>) :
        FragmentStateAdapter(this) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }
}