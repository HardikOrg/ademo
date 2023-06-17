package com.example.ademo.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ademo.network.SlusheGrabber
import com.example.ademo.utils.Account
import com.example.ademo.utils.Settings
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivityViewModel : ViewModel() {
    private val accOkHttpClient = OkHttpClient()

    private var fetchJob: Job? = null

    private val _loginCookie = MutableLiveData<String?>(null)
    val loginCookie: LiveData<String?> = _loginCookie

    val account = MediatorLiveData<Account>().apply {
        addSource(_loginCookie) {
            Log.d("MAVM", "Received cookies: ${_loginCookie.value}")
            fetchAccountDetails()
        }
    }

    fun setCookie(cookie: String) {
        _loginCookie.value = cookie
    }

    fun fetchAccountDetails() {
        with(fetchJob) {
            if (this == null || this.isCompleted) {
                fetchJob = viewModelScope.launch(CoroutineName("PlayerDataLocalFetch")) {
                    Log.d("MAVM", "Fetching Details")
                    account.value = fetchAccount()
                    Log.d("MAVM", "Account fetched: ${account.value}")
                }
            }
        }
    }

    private suspend fun fetchAccount(): Account? = withContext(Dispatchers.IO) {
        _loginCookie.value?.let {
            val request = Request.Builder()
                .url(Settings.getProfileLink(0))
                .addHeader("User-Agent", Settings.userAgent)
                .addHeader("Cookie", it)
                .build()

            val response = accOkHttpClient.newCall(request).execute()
            response.body?.let { body ->
                SlusheGrabber.getAccountDetails(body.string())
            }
        }
    }

    fun clearAccount() {
        _loginCookie.value = null
        account.value = null
    }

//    private inner class CookieInterceptor : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val original = chain.request()
//            _loginCookie.value?.let {
//                val authorized = original.newBuilder()
//                    .addHeader("Cookie", it)
//                    .build()
//                return chain.proceed(authorized)
//            }
//            return chain.proceed(original)
//        }
//    }
}