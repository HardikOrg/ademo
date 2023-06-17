package com.example.ademo.ui.login

import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.ademo.R
import com.example.ademo.databinding.FragmentLoginBinding
import com.example.ademo.ui.MainActivityViewModel
import com.example.ademo.ui.base.BaseFragment
import com.example.ademo.utils.Settings

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = requireActivity().findNavController(R.id.nav_host_fragment)

        binding.web.apply {
            clearCache(true)
            clearHistory()
            CookieManager.getInstance().apply {
                removeAllCookies { }
                removeSessionCookies { }
                flush()
            }
            webViewClient = WebViewLogginClient { cookie: String ->
                viewModel.setCookie(cookie)
                navController.popBackStack()
            }
            settings.let {
                it.userAgentString = Settings.userAgent
                it.javaScriptEnabled = true
            }
            loadUrl("https://slushe.com/login")
        }
    }
}