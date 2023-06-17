package com.example.ademo.repo

import com.example.ademo.network.SlusheGrabber
import com.example.ademo.utils.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SlusheListsRepository {
    private val maxLoadedPage = mutableListOf<Int>().apply {
        repeat(Settings.sortTypeAmount) { add(0) }
    }

    fun resetCounter(type: Int) {
        maxLoadedPage[type] = 1
    }

    suspend fun getNextList(type: Int) = withContext(Dispatchers.IO) {
        maxLoadedPage[type]++
        SlusheGrabber.parseMainFromWeb(type, maxLoadedPage[type])
    }
}