package com.example.ademo.ui.slushe.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ademo.repo.SlusheListsRepository
import com.example.ademo.utils.PageItem
import com.example.ademo.utils.Settings
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SlusheListViewModel : ViewModel() {
    private val repository = SlusheListsRepository()

    private var fetchJob: Job? = null

    private val _sortType = MutableLiveData<Int>().apply { value = 0 }
    val sortType: LiveData<Int> = _sortType

    private val lists = mutableListOf<MutableLiveData<List<PageItem>>>().apply {
        repeat(Settings.sortTypeAmount) {
            this.add(
                MutableLiveData<List<PageItem>>().apply { value = listOf() }
            )
        }
    }

    val currentList = MediatorLiveData<List<PageItem>>().apply {
        addSource(sortType) {
            this.value = lists[it].value
            if (lists[it].value!!.isEmpty()) {
                fetchNewDataForCurrentList()
            }
        }
        repeat(Settings.sortTypeAmount) {
            addSource(lists[it]) { list ->
                if (_sortType.value == it) {
                    this.value = list
                }
            }
        }
    }

    init {
        fetchNewDataForCurrentList()
    }

    fun setSortType(type: Int) {
        if (0 <= type && type < Settings.sortTypeAmount) _sortType.value = type
    }

    private fun clearCache(type: Int) {
        lists[type].value = listOf()
        repository.resetCounter(type)
    }

    fun fetchNewDataForCurrentList() = fetchNewData(sortType.value!!)
    fun fetchNewData(type: Int) {
        Log.d("LVM", "job: $fetchJob")

        with(fetchJob) {
            if (this == null || this.isCompleted) {
                fetchJob = viewModelScope.launch(CoroutineName("ListFetch")) {
                    val newList = lists[type].value!! + repository.getNextList(type)
                    lists[type].postValue(newList)
                }
            }
        }
    }
}