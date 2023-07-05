package com.example.ademo.repo

import com.example.ademo.network.SlusheGrabber
import com.example.ademo.utils.PlayerImage
import com.example.ademo.utils.PlayerItem
import com.example.ademo.utils.PlayerVideo
import com.example.ademo.utils.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import java.io.File

class PlayerDataRepository {
    suspend fun getLocalData(file: File) = withContext(Dispatchers.IO) {
        val list = mutableListOf<PlayerItem>()
        file.forEachLine {
            val content = it.split(" ")
            list.add(
                if (content[0] == "v") {
                    PlayerVideo(content[1], content[2])
                } else {
                    PlayerImage(content[1])
                }
            )
        }

        list.toList()
    }

    suspend fun saveToFile(file: File, list: List<PlayerItem>) = withContext(Dispatchers.IO) {
        list.forEach {
            file.createNewFile()
            file.appendText(
                when (it) {
                    is PlayerVideo -> {
                        "v ${it.src} ${it.preview}\n"
                    }

                    is PlayerImage -> {
                        "i ${it.preview}\n"
                    }
                }
            )
        }
    }

    suspend fun getImages() = withContext(Dispatchers.IO) {
        SlusheGrabber.parseMainFromWeb(0, 1).map { PlayerImage(it.imgLink) }
    }

    suspend fun getVideosFromPrefetched() = withContext(Dispatchers.IO) {
        Settings.preFetched.map { PlayerVideo(it.first, it.second) }
    }

    suspend fun getVideosLinks() = withContext(Dispatchers.IO) {
        SlusheGrabber.parseVideoListPage()
    }

    fun getWebDataFlow(list: List<String>) =
        list.asFlow().mapNotNull { SlusheGrabber.parseVideoPage(it) }
}