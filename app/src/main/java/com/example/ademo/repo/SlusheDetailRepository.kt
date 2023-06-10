package com.example.ademo.repo

import com.example.ademo.network.SlusheGrabber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SlusheDetailRepository {
    // =)
    suspend fun getDetails(link: String) = withContext(Dispatchers.IO) {
        SlusheGrabber.getBaseDetails(link)
    }
}