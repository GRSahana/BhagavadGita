package com.nannaapp.bhagavadgita.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.nannaapp.bhagavadgita.model.ChapterModel
import com.nannaapp.bhagavadgita.model.cache_data.VerseInfo
import com.nannaapp.bhagavadgita.model.network_data.Chapter
import com.nannaapp.bhagavadgita.repository.ChapterDetailsRepository
import com.nannaapp.bhagavadgita.util.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterDetailsViewModel @Inject
constructor(
    private val chapterDetailsRepository: ChapterDetailsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    private val _dataState: MutableLiveData<ResultOf<ChapterModel>> = MutableLiveData()
    val dataState: LiveData<ResultOf<ChapterModel>>
        get() = _dataState

    private val _favState: MutableLiveData<ResultOf<Int>> = MutableLiveData()
    val favState: LiveData<ResultOf<Int>>
        get() = _favState

    private val _verseInfoList: MutableLiveData<ResultOf<List<VerseInfo>>> = MutableLiveData()
    val verseInfoList: LiveData<ResultOf<List<VerseInfo>>>
        get() = _verseInfoList

    fun getChapterDetails(chapterNumber: Int) {
        viewModelScope.launch {

            chapterDetailsRepository.getChapterDetails(chapterNumber)
                .onEach { dataState ->
                    _dataState.value = dataState
                }
                .launchIn(viewModelScope)

        }
    }

    fun updateFavoriteStatus(id: Int) {
        viewModelScope.launch {
            chapterDetailsRepository.updateFavoriteStatus(id)
                .onEach { favState ->
                    _favState.value = favState
                }
                .launchIn(viewModelScope)
        }
    }

    fun getVerseDetails(chapterNumber: Int, verseCount : Int) {
        viewModelScope.launch {
            chapterDetailsRepository.getVerseDetails(chapterNumber, verseCount)
                .onEach { verseInfoList ->
                    _verseInfoList.value = verseInfoList
                }
                .launchIn(viewModelScope)

        }
    }
}
