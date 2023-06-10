package com.example.ademo.network

import com.example.ademo.utils.BaseDetails
import com.example.ademo.utils.PageItem
import com.example.ademo.utils.PlayerImage
import com.example.ademo.utils.PlayerVideo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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
        Jsoup.connect(path)
            .userAgent("Mozilla")
            .get()

    fun getItemsLocal(filename: String): List<PageItem> {
        val document = getHtmlLocalPage(filename)
        return parseListPage(document)
    }

    fun getItemsWeb(category: Int, page: Int): List<PageItem> {
        val document = getHtmlListPage(category, page)
        return parseListPage(document)
    }

    private fun parseListPage(document: Document): List<PageItem> {
        val section = document.getElementsByClass("items-floating   cf  ")
        val galList = section.select("a[href*=com/galleries]")

        val list = mutableListOf<PageItem>()

        galList.forEach {
            val images = it.select("img")
            if (images.size != 2) throw Error()

            list.add(
                PageItem(
                    it.attr("href"), // post src
                    images[0].attr("src"), // img src
                    it.getElementsByClass("title").text(), // title
                    it.getElementsByClass("author").text(), // author
                    images[1].attr("src") // author img
                )
            )
        }

        return list
    }

    fun getBaseDetails(link: String): BaseDetails {
        val document = getHtmlPage(link)

        val authorBox = document.getElementsByClass("box user-info-box")
        val contentDetails = document.getElementsByClass("content-details")
        val statsBox = document.getElementsByClass("stats-col").select("span")

        val stats = mutableListOf<String>()
        for (i in 0..3) {
            stats += statsBox[i * 2 + 1].text()
        }

        return BaseDetails(
            document.getElementsByClass("big-photo").attr("src"),
            authorBox.select("img[src*=.webp]").attr("src"),
            contentDetails.select("h1").text(),
            authorBox.select("h1").text(),
            authorBox.select("p").text(),
            document.getElementsByClass("tags-list").map { it.text() },
            stats
        )
    }

    fun getPagesWithVideosList(): List<String> {
        val document = getHtmlPage(urlForVideos)

        val videosDiv = document.getElementsByClass("thumb-holder max-vid-1")
        val videos = videosDiv.select("a[href*=video]")

        return videos.map { it.attr("href") }
    }

    fun getVideoLinkFromPage(page: String): PlayerVideo {
        val document = getHtmlPage(page)

        val video = document.select("video")
        val cloudIdElement = video.prev().prev()

        val resString = cloudIdElement.toString().take(100).split("'")

        val srcBigString = video.next().toString().take(1200)
        val srcLineString = Regex("[^/]const source.*;").find(srcBigString)!!.value
        val srcList = srcLineString.split("'")

        return PlayerVideo(srcList[1] + resString[1] + srcList[3], video.attr("poster"))
    }

    fun getVideosLinksFromPage(list: List<String>) = list.map { getVideoLinkFromPage(it) }
    fun getImagesLinksFromPage() = getItemsWeb(0, 1).map { PlayerImage(it.imgLink) }
}