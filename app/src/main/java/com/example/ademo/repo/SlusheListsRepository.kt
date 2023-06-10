package com.example.ademo.repo

import com.example.ademo.network.SlusheGrabber
import com.example.ademo.utils.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SlusheListsRepository {
    val maxLoadedPage = mutableListOf<Int>().apply {
        repeat(Settings.sortTypeAmount) { add(1) }
    }

    fun resetCounter(type: Int) {
        maxLoadedPage[type] = 1
    }

    suspend fun getNextList(type: Int) = withContext(Dispatchers.IO) {
        SlusheGrabber.getItemsWeb(type, maxLoadedPage[type]++)
    }
}