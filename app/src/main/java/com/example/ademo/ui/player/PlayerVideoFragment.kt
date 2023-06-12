package com.example.ademo.ui.player

import android.os.Bundle
import android.view.View
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.ademo.R
import com.example.ademo.databinding.FragmentPlayerBinding
import com.example.ademo.databinding.PlayerItemBinding
import com.example.ademo.ui.base.BaseFragment
import com.example.ademo.ui.base.BaseRecyclerAdapter
import com.example.ademo.utils.Settings

class PlayerVideoFragment : BaseFragment<FragmentPlayerBinding>(FragmentPlayerBinding::inflate) {
    private val viewModel by navGraphViewModels<PlayerViewModel>(R.id.nav_player_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        val playlist = viewModel.getPlaylistForPlayer()
        val previews = playlist.map { it.mediaId }.toMutableList()

        player.setMediaItems(playlist)
        player.prepare()

        var currentIndex = player.currentMediaItemIndex

        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val cur = player.currentMediaItemIndex

                binding.queue.layoutManager?.apply {
                    findViewByPosition(currentIndex)?.let {
                        val vb = PlayerItemBinding.bind(it)
                        vb.playButton.visibility = View.INVISIBLE
                    }
                    findViewByPosition(cur)?.let {
                        val vb = PlayerItemBinding.bind(it)
                        vb.playButton.visibility = View.VISIBLE
                    }
                }
                (binding.queue.adapter as QueueAdapter).apply {
                    updateElements()
                }
                currentIndex = cur
            }
        })

        binding.queue.apply {
            layoutManager = GridLayoutManager(view.context, Settings.recyclerGridWidth)
            adapter = QueueAdapter().apply { setData(previews) }
        }

//        binding.queue.doOnNextLayout {
//            binding.queue.getChildAt(0)?.apply {
//                PlayerItemBinding.bind(this).playButton.visibility = View.VISIBLE
//            }
//            (binding.queue.adapter as QueueAdapter).updateElements()
//        }
    }

    override fun onStop() {
        super.onStop()
        binding.playerView.player?.release()
    }

    inner class QueueAdapter :
        BaseRecyclerAdapter<String, PlayerItemBinding>(PlayerItemBinding::inflate) {

        override fun bind(binding: PlayerItemBinding, item: String, id: Int) {
            super.bind(binding, item, id)
            val placeholder = CircularProgressDrawable(binding.root.context).apply {
                setColorSchemeColors(binding.root.context.resources.getColor(R.color.s_pink))
                strokeWidth = 5f
                centerRadius = 30f
                start()
            }

            Glide.with(binding.root)
                .load(item)
                .placeholder(placeholder)
                .centerCrop()
                .into(binding.image)

            binding.imageButton.visibility = View.GONE
            binding.imageForegorund.visibility = View.GONE
        }

        fun updateElements() {
            notifyDataSetChanged()
        }
    }
}