package com.nannaapp.bhagavadgita.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.nannaapp.bhagavadgita.model.network_data.Slok
import com.nannaapp.bhagavadgita.repository.MainRepository
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
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    private val _dataState: MutableLiveData<ResultOf<Slok>> = MutableLiveData()
    val dataState: LiveData<ResultOf<Slok>>
        get() = _dataState

    fun getVerseDetails(chapterNumber: Int, verseNumber: Int) {
        viewModelScope.launch {

            mainRepository.getVerseDetails(chapterNumber, verseNumber)
                .onEach { dataState ->
                    _dataState.value = dataState
                    Log.d("Data", ": $dataState")
                }
                .launchIn(viewModelScope)

        }
    }
}

