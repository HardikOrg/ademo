package com.example.ademo.ui.login

import android.util.JsonReader
import android.util.JsonToken
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.ademo.network.SlusheGrabber

class WebViewLogginClient(val callback: (String) -> Unit) : WebViewClient() {
    var currentCookie: String? = null

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val check = listOf("PHPSESSID", "_gid", "_ga")

        CookieManager.getInstance().getCookie(request!!.url.toString())?.apply {
            if (check.fold(true) { it, a -> it and this.contains(a) }) {
                Log.d("WVC", "found loginCookie: $this")
                currentCookie = this
            }
        }

        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        currentCookie?.let { cookie ->
            view?.evaluateJavascript("(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();") {
                it?.let { str ->
                    val reader = JsonReader(str.reader()).apply { isLenient = true }

                    if (reader.peek() == JsonToken.STRING) {
                        reader.nextString()?.let { html ->
                            if (SlusheGrabber.hasAccountString(html)) {
                                callback(cookie)
                            }
                        }
                    }

                    reader.close()
                }
            }
        }

        super.onPageFinished(view, url)
    }
}