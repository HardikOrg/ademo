package com.example.ademo.utils

object Settings {
    const val mainUrl = "https://slushe.com"
    const val videosUrl = "https://slushe.com/HentaiVR"

    val categories = listOf(
        "featured", "most-recent", "most-likes", "most-viewed", "most-discussed", "list-posts"
    )
    val sortTypeAmount = categories.size

    const val imagesPerPage = 20
    const val recyclerGridWidth = 3

    const val fetchedDataFilename = "data.txt"

    const val userAgent =
        "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.3"

    val profileLinks = listOf(
        "edit-profile/personal-info", "upload_photo", "manage-posts", "favorites/",
        "edit-profile", "mailbox/", "notifications/", "logout"
    )

    fun getLinkForList(category: Int, page: Int) =
        "${mainUrl}/${categories[category]}" + if (page != 1) "/page$page.html" else ""

    fun getProfileLink(id: Int) =
        "${mainUrl}/${profileLinks[id]}"
}