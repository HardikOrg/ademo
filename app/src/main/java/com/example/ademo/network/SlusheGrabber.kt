package com.example.ademo.network

import android.util.Log
import com.example.ademo.utils.BaseDetails
import com.example.ademo.utils.PageItem
import com.example.ademo.utils.PlayerImage
import com.example.ademo.utils.PlayerVideo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File

object SlusheGrabber {
    private const val url = "https://slushe.com"
    private const val urlForVideos = "https://slushe.com/HentaiVR"

    private val categories = listOf(
        "featured", "most-recent", "most-likes", "most-viewed", "most-discussed", "list-posts"
    )

    private fun getLinkForList(category: Int, page: Int) =
        "$url/${categories[category]}" + if (page != 1) "/page$page.html" else ""

    fun getHtmlLocalPage(filename: String) =
        Jsoup.parse(File(filename), "UTF-8")

    fun getHtmlListPage(category: Int = 0, page: Int) =
        getHtmlPage(getLinkForList(category, page))

    fun getHtmlPage(path: String) =
        try {
            Jsoup.connect(path)
                .userAgent("Mozilla")
                .get()
        } catch (e: Exception) {
            Log.d("SG", "Can't get the document from $path: $e")
            null
        }

    fun getItemsLocal(filename: String) =
        parseListPage(getHtmlLocalPage(filename))

    fun getItemsWeb(category: Int, page: Int): List<PageItem> {
        val document = getHtmlListPage(category, page)

        return if (document != null) parseListPage(document) else listOf()
    }

    private fun parseListPage(document: Document): List<PageItem> {
        return try {
            val section = document.getElementsByClass("items-floating   cf  ")
            val galList = section.select("a[href*=com/galleries]")

            galList.map {
                val images = it.select("img")

                PageItem(
                    it.attr("href"), // post src
                    images[0].attr("src"), // img src
                    it.getElementsByClass("title").text(), // title
                    it.getElementsByClass("author").text(), // author
                    images[1].attr("src") // author img
                )
            }
        } catch (e: Exception) {
            Log.e("Grabber", "parseListPage parsing: $e")
            listOf()
        }
    }

    fun getBaseDetails(link: String): BaseDetails? {
        return try {
            val document = getHtmlPage(link) ?: return null

            val authorBox = document.getElementsByClass("box user-info-box")
            val contentDetails = document.getElementsByClass("content-details")
            val statsBox = document.getElementsByClass("stats-col").select("span")

            val stats = mutableListOf<String>()
            repeat(4) { stats += statsBox[it * 2 + 1].text() }

            BaseDetails(
                document.getElementsByClass("big-photo").attr("src"),
                authorBox.select("img[src*=.webp]").attr("src"),
                contentDetails.select("h1").text(),
                authorBox.select("h1").text(),
                authorBox.select("p").text(),
                document.getElementsByClass("tags-list").map { it.text() },
                stats
            )
        } catch (e: Exception) {
            Log.e("Grabber", "getBaseDetails parsing: $e")
            null
        }
    }

    fun getPagesWithVideosList(): List<String> {
        val videos: Elements

        try {
            val document = getHtmlPage(urlForVideos) ?: return listOf()

            val videosDiv = document.getElementsByClass("thumb-holder max-vid-1")
            videos = videosDiv.select("a[href*=video]")
        } catch (e: Exception) {
            Log.e("Grabber", "getPagesWithVideosList parsing: $e")
            return listOf()
        }

        return videos.mapNotNull {
            try {
                it.attr("href")
            } catch (e: Exception) {
                Log.e("Grabber", "getPagesWithVideosList parsing: $e")
                null
            }
        }
    }

    fun getVideoLinkFromPage(page: String): PlayerVideo? {
        return try {
            val document = getHtmlPage(page) ?: return null

            val video = document.select("video")
            val cloudIdElement = video.prev().prev()

            val resString = cloudIdElement.toString().take(100).split("'")

            val srcBigString = video.next().toString().take(1200)
            val srcLineString = Regex("[^/]const source.*;").find(srcBigString)!!.value
            val srcList = srcLineString.split("'")

            PlayerVideo(srcList[1] + resString[1] + srcList[3], video.attr("poster"))
        } catch (e: Exception) {
            Log.e("Grabber", "getPagesWithVideosList parsing: $e")
            null
        }
    }

    fun getVideosLinksFromPage(list: List<String>) = list.mapNotNull { getVideoLinkFromPage(it) }
    fun getImagesLinksFromPage() = getItemsWeb(0, 1).map { PlayerImage(it.imgLink) }
}