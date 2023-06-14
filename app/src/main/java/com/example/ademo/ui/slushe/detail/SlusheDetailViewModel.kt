package com.example.ademo.ui.slushe.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ademo.repo.SlusheDetailRepository
import com.example.ademo.utils.BaseDetails
import com.example.ademo.utils.PageItem
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SlusheDetailViewModel : ViewModel() {
    private lateinit var pageItem: PageItem
    private val repository = SlusheDetailRepository()

    private val _details = MutableLiveData<BaseDetails>()
    val details: LiveData<BaseDetails> = _details

    private var fetchJob: Job? = null

    fun setPageItem(newPageItem: PageItem) {
        pageItem = newPageItem
        fetchDetails()
    }

    private fun fetchDetails() {
        Log.d("DVM", "job: $fetchJob")

        with(fetchJob) {
            // Refresh™ approved =)
            if (this == null || this.isCompleted) {
                fetchJob = viewModelScope.launch(CoroutineName("DetailsFetch")) {
                    val res = repository.getDetails(pageItem.srcLink)
                    if (res != null) {
                        _details.value = res!!
                    } else {
                        Log.d("DVM", "Can't load detail page")
                    }
                }
            }
        }
    }
}