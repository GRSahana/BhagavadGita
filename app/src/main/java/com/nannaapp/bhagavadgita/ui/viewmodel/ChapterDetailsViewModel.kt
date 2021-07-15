package com.nannaapp.bhagavadgita.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.nannaapp.bhagavadgita.model.network_data.Chapter
import com.nannaapp.bhagavadgita.repository.MainRepository
import com.nannaapp.bhagavadgita.util.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterDetailsViewModel @Inject
constructor(
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    private val _dataState: MutableLiveData<ResultOf<Chapter>> = MutableLiveData()
    val dataState: LiveData<ResultOf<Chapter>>
        get() = _dataState

    fun getChapterDetails(chapterNumber: Int) {
        viewModelScope.launch {

            mainRepository.getChapterDetails(chapterNumber)
                .onEach { dataState ->
                    _dataState.value = dataState
                    Log.d("Data", ": $dataState")
                }
                .launchIn(viewModelScope)

        }
    }
}
