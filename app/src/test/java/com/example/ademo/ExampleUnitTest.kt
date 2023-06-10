package com.example.ademo

import com.example.ademo.network.SlusheGrabber
import com.example.ademo.utils.PageItem
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

    private fun executeAndFormat(code: () -> Unit) {
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
                val a = SlusheGrabber.getHtmlListPage(0, page)
                file.writeText(a.toString())
            }
        }

        executeAndFormat {
            var sum = Duration.ZERO

            for (page in 1..10) {
                val time = measureTime {
                    val res = SlusheGrabber.getItemsLocal("$path$name$page")
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
                list = SlusheGrabber.getItemsWeb(0, 1)
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
                    SlusheGrabber.getItemsWeb(0, page)
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

        val a = SlusheGrabber.getBaseDetails(src)

        executeAndFormat {
            println(a)
        }
    }

    @Test
    fun checkVideoPageListGrabber() {
        executeAndFormat {
            val a = SlusheGrabber.getPagesWithVideosList()

            println(a.size)
            a.forEach { println(it) }
        }
    }

    @Test
    fun checkVideoGrabber() {
        val url = "https://slushe.com/video/chole-and-kara-testing-179931.html"

        executeAndFormat {
            val a = SlusheGrabber.getVideoLinkFromPage(url)
            println(a)
        }
    }

    // ~ 1-2 minute long for 20 pages
    @Test
    fun checkVideoSGrabber() {
        executeAndFormat {
            val a = SlusheGrabber.getPagesWithVideosList()
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
}