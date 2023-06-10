package com.example.ademo.ui.slushe.list

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ademo.R
import com.example.ademo.databinding.FragmentSlusheListBinding
import com.example.ademo.ui.MainActivity
import com.example.ademo.ui.base.BaseFragment
import com.example.ademo.utils.PageItem
import com.example.ademo.utils.Settings

class SlusheListFragment :
    BaseFragment<FragmentSlusheListBinding>(FragmentSlusheListBinding::inflate) {
    // ViewModel is scoped to the nested slushe graph -> list and details
    private val viewModel by navGraphViewModels<SlusheListViewModel>(R.id.nav_slushe_graph)

    private val onRecyclerItemClick = { item: PageItem ->
        val action = SlusheListFragmentDirections.actionNavSlusheToSlusheDetailFragment(item)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sortList = requireContext().resources.getStringArray(R.array.sort_types)

        binding.recylerView.apply {
            layoutManager = GridLayoutManager(view.context, Settings.recyclerGridWidth)
            adapter = SlusheGalleryAdapter(onRecyclerItemClick)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    val itemCount = (recyclerView.adapter as SlusheGalleryAdapter).itemCount
                    val lastVisible =
                        (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()

                    if (itemCount - lastVisible <= Settings.imagesPerPage) {
                        viewModel.fetchNewDataForCurrentList()
                    }
                }
            })
        }

        viewModel.sortType.observe(viewLifecycleOwner) {
            binding.toolbar.title = sortList[it]
        }

        viewModel.currentList.observe(viewLifecycleOwner) {
            (binding.recylerView.adapter as SlusheGalleryAdapter).setData(it)
        }

        binding.toolbar.apply {
            inflateMenu(R.menu.slushe_sort_menu)
            overflowIcon = ContextCompat.getDrawable(requireContext(), R.drawable.icon_filter)

            setOnMenuItemClickListener {
                viewModel.setSortType(sortList.indexOf(it.title))
                true
            }
            setNavigationOnClickListener {
                (requireActivity() as MainActivity).onSupportNavigateUp()
            }
        }
    }
}
