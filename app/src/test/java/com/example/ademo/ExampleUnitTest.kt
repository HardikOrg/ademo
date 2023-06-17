package com.example.ademo

import com.example.ademo.network.SlusheGrabber
import com.example.ademo.utils.PageItem
import com.example.ademo.utils.Settings
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@OptIn(ExperimentalTime::class)
class ExampleUnitTest {
    private val categories = listOf(
        "featured", "most-recent", "most-likes", "most-viewed", "most-discussed", "list-posts"
    )

    inline fun executeAndFormat(code: () -> Unit) {
        println()
        code.invoke()
        println()
    }

    // ~ 100ms parse for 10 pages
    @Test
    fun checkJsoupSpeed() {
        val path = "D:\\Projects\\ADemo\\app\\src\\test\\java\\com\\example\\ademo\\testHtmls\\"
        val name = "test"

        for (page in 1..10) {
            val filename = "$path$name$page"
            val file = File(filename)

            if (!file.exists()) {
                val a = SlusheGrabber.getDocForMain(0, page)
                file.writeText(a.toString())
            }
        }

        executeAndFormat {
            var sum = Duration.ZERO

            for (page in 1..10) {
                val time = measureTime {
                    SlusheGrabber.parseMainFromLocal("$path$name$page")
                }
                println("page $page time:$time")
                sum += time
            }
            println("Total: $sum")
        }
    }

    // ~ 1-3s
    @Test
    fun checkFirstPageFetch() {
        executeAndFormat {
            var list: List<PageItem>
            val time = measureTime {
                list = SlusheGrabber.parseMainFromWeb(0, 1)
            }

            list.forEach { println(it) }
            println("elapsed: $time")
        }
    }


    // ~ 8-16s
    @Test
    fun checkMutliplePagesFetching() {
        executeAndFormat {
            var sum = Duration.ZERO
            for (page in 1..11) {
                val time = measureTime {
                    SlusheGrabber.parseMainFromWeb(0, page)
                }
                println("page $page time:$time")
                sum += time
            }
            println("Total: $sum")
        }
    }

    @Test
    fun checkDetailsGrabber() {
        val src = "https://slushe.com/galleries/ada-wong-179306.html"

        val a = SlusheGrabber.parsePostDetails(src)

        executeAndFormat {
            println(a)
        }
    }

    @Test
    fun checkVideoPageListGrabber() {
        executeAndFormat {
            val a = SlusheGrabber.parseVideoListPage()

            println(a.size)
            a.forEach { println(it) }
        }
    }

    @Test
    fun checkVideoGrabber() {
        val url = "https://slushe.com/video/chole-and-kara-testing-179931.html"

        executeAndFormat {
            val a = SlusheGrabber.parseVideoPage(url)
            println(a)
        }
    }

    // ~ 1-2 minute long for 20 pages
    @Test
    fun checkVideoSGrabber() {
        executeAndFormat {
            val a = SlusheGrabber.parseVideoListPage()
            val b = SlusheGrabber.getVideosLinksFromPage(a.take(4))

            b.forEach { println(it) }
        }
    }

    @Test
    fun checkImagesGrabber() {
        executeAndFormat {
            val a = SlusheGrabber.getImagesLinksFromPage()

            a.forEach { println(it) }
        }
    }

    @Test
    fun checkHasAccount() {
        executeAndFormat {
            val client = OkHttpClient.Builder()
//                .addInterceptor(LoggingInterceptor())
                .build()

            val request = Request.Builder()
                .url("https://slushe.com")
                .addHeader("User-Agent", Settings.userAgent)
                .addHeader("Cookie", "PHPSESSID=2j0bt9lv21h1d91ee0ngva8ss4")
                .build()

            val response = client.newCall(request).execute()

            response.body?.let {
                val str = SlusheGrabber.hasAccountString(it.string())
                println(str)
            }
        }
    }

    @Test
    fun checkAccountDetails() {
        val client = OkHttpClient.Builder()
            .build()

        val request = Request.Builder()
            .url("https://slushe.com/${Settings.profileLinks[0]}")
            .addHeader("User-Agent", Settings.userAgent)
            .addHeader("Cookie", "PHPSESSID=2j0bt9lv21h1d91ee0ngva8ss4")
            .build()

        val response = client.newCall(request).execute()
        response.body?.let {
            val str = SlusheGrabber.getAccountDetails(it.string())
            println(str)
        }
    }

    class LoggingInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            println("REQUEST (${request.method} method --> ${request.url})")
            request.headers.forEach {
                println(
                    String.format(
                        "\t%26s -- %s", it.first, it.second
                    )
                )
            }

            println()

            val response = chain.proceed(request)
            println("RESPONSE (${response.protocol} protocol)")
            response.headers.forEach {
                println(
                    String.format(
                        "\t%26s -- %s", it.first, it.second
                    )
                )
            }

            println()

            return response
        }
    }
}