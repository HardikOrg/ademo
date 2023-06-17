package com.example.ademo.utils

import java.io.Serializable

data class BaseDetails(
    val bigImageSrc: String,
    val authorImageSrc: String,
    val title: String,
    val authorName: String,
    val authorPosition: String,
    val tags: List<String>,
    val stats: List<String>
) : Serializable

data class PageItem(
    val srcLink: String,
    val imgLink: String,
    val title: String,
    val author: String,
    val authorImgLink: String
) : Serializable


data class Account(
    val accountLink: String,
    val imgLink: String,
    val name: String,
    val position: String,
    val email: String
) : Serializable


sealed class PlayerItem {
    abstract val preview: String
    var isLiked: Boolean = false
}

data class PlayerVideo(
    val src: String,
    override val preview: String
) : PlayerItem(), Serializable

data class PlayerImage(
    override val preview: String,
) : PlayerItem(), Serializable