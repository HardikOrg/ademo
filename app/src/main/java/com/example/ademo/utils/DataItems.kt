package com.example.ademo.utils

import java.io.Serializable

data class BaseDetails(
    var bigImageSrc: String,
    var authorImageSrc: String,
    var title: String,
    var authorName: String,
    var authorPosition: String,
    var tags: List<String>,
    var stats: List<String>
) : Serializable

data class PageItem(
    var srcLink: String,
    var imgLink: String,
    var title: String,
    var author: String,
    var authorImgLink: String
) : Serializable

sealed class PlayerItem {
    abstract val preview: String
}

data class PlayerVideo(
    val src: String,
    override val preview: String
) : PlayerItem(), Serializable

data class PlayerImage(
    override val preview: String,
) : PlayerItem(), Serializable