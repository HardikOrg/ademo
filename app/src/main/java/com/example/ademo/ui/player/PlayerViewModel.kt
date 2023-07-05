package com.example.ademo.ui.player

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.ademo.repo.PlayerDataRepository
import com.example.ademo.utils.PlayerItem
import com.example.ademo.utils.PlayerVideo
import com.example.ademo.utils.Settings
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PlayerViewModel : ViewModel() {
    private val repository = PlayerDataRepository()

    private var fetchJob: Job? = null

    private val _listContent = MutableLiveData<List<PlayerItem>>(listOf())
    val listContent: LiveData<List<PlayerItem>> = _listContent

    private val _playlist = MutableLiveData<List<PlayerItem>>(listOf())
    val playlist: LiveData<List<PlayerItem>> = _playlist

    val addClick = { _: PlayerItem, id: Int ->
        _listContent.value = _listContent.value!!.also {
            // update
            it[id].isLiked = true

            val item = it[id]
            if (!_playlist.value!!.contains(item)) {
                _playlist.value = _playlist.value!! + item
            }
        }
    }

    val removeClick = { _: PlayerItem, id: Int ->
        val new = _playlist.value!!.toMutableList()
        val item = new.removeAt(id)
        _playlist.value = new

        val index = _listContent.value!!.indexOf(item)
        _listContent.value = _listContent.value!!.also {
             it[index].isLiked = false
        }
    }

    fun getVideosFromPlaylist() = _playlist.value!!.filterIsInstance<PlayerVideo>()
    fun getPlaylistForPlayer() = getVideosFromPlaylist().map {
        MediaItem.Builder().setUri(it.src).setMediaId(it.preview).build()
    }

    fun getDataFromFile(file: File) {
        with(fetchJob) {
            if (this == null || this.isCompleted) {
                fetchJob = viewModelScope.launch(CoroutineName("PlayerDataLocalFetch")) {
                    _listContent.value = repository.getLocalData(file)
                }
            }
        }
    }

    fun fetchAndSaveData(usePrefetched: Boolean, file: File) {
        with(fetchJob) {
            if (this == null || this.isCompleted) {
                fetchJob = viewModelScope.launch(CoroutineName("PlayerDataFetch")) {
                    if (usePrefetched) {
                        val list = repository.getVideosFromPrefetched()
                        _listContent.value = list
                    }

                    if (_listContent.value!!.size <= Settings.prefetchedVerify) {
                        Log.d("PVM", "Trying to fetch")
                        val list = repository.getVideosLinks()
                        Log.d("PVM", "getVideosLinks done")

                        val flow = repository.getWebDataFlow(list)

                        withContext(Dispatchers.IO) {
                            flow.collect {
                                Log.d("PVM", "video added: ${it.src}")

                                withContext(Dispatchers.Main) {
                                    _listContent.value = _listContent.value!! + it
                                }
                            }
                        }
                    }
                    repository.saveToFile(file, _listContent.value!!)
                }
            }
        }
    }
}