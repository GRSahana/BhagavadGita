package com.nannaapp.bhagavadgita.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.nannaapp.bhagavadgita.model.network_data.Slok
import com.nannaapp.bhagavadgita.repository.MainRepository
import com.nannaapp.bhagavadgita.repository.VerseDetailsRepository
import com.nannaapp.bhagavadgita.util.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerseDetailsViewModel @Inject
constructor(
    private val verseDetailsRepository: VerseDetailsRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    private val _dataState: MutableLiveData<ResultOf<Slok>> = MutableLiveData()
    val dataState: LiveData<ResultOf<Slok>>
        get() = _dataState

    private val _readStatus: MutableLiveData<ResultOf<Int>> = MutableLiveData()
    val readStatus: LiveData<ResultOf<Int>>
        get() = _readStatus

    fun getVerseDetails(chapterNumber: Int, verseNumber: Int) {
        viewModelScope.launch {

            verseDetailsRepository.getVerseDetails(chapterNumber, verseNumber)
                .onEach { dataState ->
                    _dataState.value = dataState
                }
                .launchIn(viewModelScope)

        }
    }

    fun updateReadStatus(chapterNumber: Int, verseNumber: Int) {
        viewModelScope.launch {

            verseDetailsRepository.updateReadStatus(chapterNumber, verseNumber)
                .onEach { readStatus ->
                    _readStatus.value = readStatus
                }
                .launchIn(viewModelScope)

        }
    }
}

