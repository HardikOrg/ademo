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
    const val prefetchedVerify = 10
    val preFetched = listOf(
        "https://videodelivery.net/24734e60f3a74ac88d9e038eadfac9d1/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/3839649c18b181f5c.mp4/3839649c18b181f5c.mp4-1.jpg",
        "https://videodelivery.net/0790d7d6b76d433cbe3718074164565c/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/383964995ab65c689.mp4/383964995ab65c689.mp4-1.jpg",
        "https://videodelivery.net/9c8bad131ac645f690ef100752088a8c/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/383964956486071d5.mp4/383964956486071d5.mp4-1.jpg",
        "https://videodelivery.net/1601d3f3f23947cbbcc2e8b088c6eb4e/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/38396492bffcb0268.mp4/38396492bffcb0268.mp4-1.jpg",
        "https://videodelivery.net/13f000fbd1df4f329a3af9db23c8e22f/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/3839648c185be8410.mp4/3839648c185be8410.mp4-1.jpg",
        "https://videodelivery.net/f090da304db04c9198266dee2b953afd/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/38396489847caf9d4.mp4/38396489847caf9d4.mp4-1.jpg",
        "https://videodelivery.net/9a9d6d4aad064831ab280bc750da8255/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/38396486d64f7aaaf.mp4/38396486d64f7aaaf.mp4-1.jpg",
        "https://videodelivery.net/1ff29dea12984ba98718a5fe922950f2/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/3839648067f849196.mp4/3839648067f849196.mp4-1.jpg",
        "https://videodelivery.net/c7c014a2c21c4e21a3ab5461efa9152e/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/3839647d9f6470a03.mp4/3839647d9f6470a03.mp4-1.jpg",
        "https://videodelivery.net/aaa0a470659d43d9b3f6d411e1808884/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/38396479c675ec6a9.mp4/38396479c675ec6a9.mp4-1.jpg",
        "https://videodelivery.net/8520c510b3f54c53a9ba71446436486d/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/38396475b701d4cb9.mp4/38396475b701d4cb9.mp4-1.jpg",
        "https://videodelivery.net/1bd478baadba4a6f996fd53677165bfe/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/383964708412311b9.mp4/383964708412311b9.mp4-1.jpg",
        "https://videodelivery.net/e57fa1a0f65145c4a027c26d162ccf5d/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/383964673739623d8.mp4/383964673739623d8.mp4-1.jpg",
        "https://videodelivery.net/24f001dfa8a94fbcbe093d8a195aaf83/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/38396461e2338c370.mp4/38396461e2338c370.mp4-1.jpg",
        "https://videodelivery.net/03ac699b5118437a99be7ca7b9fd211e/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/3839645e1ba838b7b.mp4/3839645e1ba838b7b.mp4-1.jpg",
        "https://videodelivery.net/b63bddf5968a4a8bbf8d8627d707a5da/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/3839645378c080bd1.mp4/3839645378c080bd1.mp4-1.jpg",
        "https://videodelivery.net/744e40e8a11648e0b2bffe36dfbdce3c/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/38396450e053ae918.mp4/38396450e053ae918.mp4-1.jpg",
        "https://videodelivery.net/5117a43a5dc3427e83fb15ed41c3b302/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/383964424a4154226.mp4/383964424a4154226.mp4-1.jpg",
        "https://videodelivery.net/42abaabd72ca43edab1ab8c8437c1700/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/3839643d07d36a8ab.mp4/3839643d07d36a8ab.mp4-1.jpg",
        "https://videodelivery.net/2cb6d49dcbcf4027a362c735c0d0518f/manifest/video.m3u8" to
                "https://cdn.slushe.com/thumbs/3/8/3/9/6/3839643923076c910.mp4/3839643923076c910.mp4-1.jpg"
    )
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