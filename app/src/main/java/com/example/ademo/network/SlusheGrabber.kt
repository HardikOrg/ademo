package com.example.ademo.network

import android.util.Log
import com.example.ademo.utils.Account
import com.example.ademo.utils.BaseDetails
import com.example.ademo.utils.PageItem
import com.example.ademo.utils.PlayerImage
import com.example.ademo.utils.PlayerVideo
import com.example.ademo.utils.Settings
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File

object SlusheGrabber {
    fun getDocFromString(string: String) =
        Jsoup.parse(string)

    fun getDocFromFile(filename: String) =
        Jsoup.parse(File(filename), "UTF-8")

    fun getDocFromUrl(url: String) =
        try {
            Jsoup.connect(url)
                .userAgent("Mozilla")
                .get()
        } catch (e: Exception) {
            Log.d("SG", "Can't get the document from $url: $e")
            null
        }

    fun getDocForMain(category: Int = 0, page: Int) =
        getDocFromUrl(Settings.getLinkForList(category, page))

    fun hasAccountString(string: String) =
        try {
            val document = getDocFromString(string)
            val a = document.getElementsByClass("my-profile-wrapper").select("a")

            a.attr("href").isNotEmpty()
        } catch (e: Exception) {
            false
        }

    fun getAccountDetails(html: String) =
        try {
            val document = getDocFromString(html)
            val top = document.getElementsByClass("top")
            val links = top.select("a[href]")

            Account(
                accountLink = links[0].attr("href"),
                email = document.select("input[placeholder='Email']")[0].attr("value"),
                imgLink = links[0].select("img").attr("src"),
                name = links[1].text(),
                position = top.select("p").text()
            )
        } catch (e: Exception) {
            Log.d("SG", "getAccountDetails: $e")
            null
        }

    fun parseMainFromLocal(filename: String) =
        parseMainPage(getDocFromFile(filename))

    fun parseMainFromWeb(category: Int, page: Int): List<PageItem> {
        val document = getDocForMain(category, page)

        return if (document != null) parseMainPage(document) else listOf()
    }

    private fun parseMainPage(document: Document) =
        try {
            val section = document.getElementsByClass("items-floating   cf  ")
            val galList = section.select("a[href*=com/galleries]")

            galList.map {
                val images = it.select("img")

                PageItem(
                    srcLink = it.attr("href"), // post src
                    imgLink = images[0].attr("src"), // img src
                    title = it.getElementsByClass("title").text(), // title
                    author = it.getElementsByClass("author").text(), // author
                    authorImgLink = images[1].attr("src") // author img
                )
            }
        } catch (e: Exception) {
            Log.e("Grabber", "parseListPage parsing: $e")
            listOf()
        }

    fun parsePostDetails(link: String): BaseDetails? {
        return try {
            val document = getDocFromUrl(link) ?: return null

            val authorBox = document.getElementsByClass("box user-info-box")
            val contentDetails = document.getElementsByClass("content-details")
            val statsBox = document.getElementsByClass("stats-col").select("span")

            val stats = mutableListOf<String>()
            repeat(4) { stats += statsBox[it * 2 + 1].text() }

            BaseDetails(
                bigImageSrc = document.getElementsByClass("big-photo").attr("src"),
                authorImageSrc = authorBox.select("img[src*=.webp]").attr("src"),
                title = contentDetails.select("h1").text(),
                authorName = authorBox.select("h1").text(),
                authorPosition = authorBox.select("p").text(),
                tags = document.getElementsByClass("tags-list").map { it.text() },
                stats = stats
            )
        } catch (e: Exception) {
            Log.e("Grabber", "getBaseDetails parsing: $e")
            null
        }
    }

    fun parseVideoListPage(): List<String> {
        val videos: Elements

        try {
            val document = getDocFromUrl(Settings.videosUrl) ?: return listOf()

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

    fun parseVideoPage(page: String): PlayerVideo? {
        return try {
            val document = getDocFromUrl(page) ?: return null

            val video = document.select("video")
            val cloudIdElement = video.prev().prev()

            val resString = cloudIdElement.toString().take(100).split("'")

            val srcBigString = video.next().toString().take(1200)
            val srcLineString = Regex("[^/]const source.*;").find(srcBigString)!!.value
            val srcList = srcLineString.split("'")

            PlayerVideo(
                src = srcList[1] + resString[1] + srcList[3],
                preview = video.attr("poster")
            )
        } catch (e: Exception) {
            Log.e("Grabber", "getPagesWithVideosList parsing: $e")
            null
        }
    }

    fun getVideosLinksFromPage(list: List<String>) = list.mapNotNull { parseVideoPage(it) }
    fun getImagesLinksFromPage() = parseMainFromWeb(0, 1).map { PlayerImage(it.imgLink) }
}