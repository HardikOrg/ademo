package com.example.ademo.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.ademo.databinding.FragmentHomeBinding
import com.example.ademo.ui.MainActivity
import com.example.ademo.ui.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.text.observe(viewLifecycleOwner) { newStr ->
            binding.textView.text = newStr
        }

        binding.toolbar.setNavigationOnClickListener {
            (requireActivity() as MainActivity).onSupportNavigateUp()
        }
    }
}
