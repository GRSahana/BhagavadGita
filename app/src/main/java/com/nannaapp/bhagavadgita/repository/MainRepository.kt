package com.nannaapp.bhagavadgita.repository

import com.nannaapp.bhagavadgita.model.network_data.Chapter
import com.nannaapp.bhagavadgita.model.network_data.Slok
import com.nannaapp.bhagavadgita.network.BhagavadgitaApi
import com.nannaapp.bhagavadgita.util.ResultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class MainRepository
constructor(
    private val bhagavadgitaApi: BhagavadgitaApi
) {

    suspend fun getChapterList(): Flow<ResultOf<List<Chapter>>> = flow {
        emit(ResultOf.Loading)
        try {
            val result = bhagavadgitaApi.getChaptersList()

            emit(ResultOf.Success(result))
        } catch (e: Exception) {
            emit(ResultOf.Error.Error1(e))

        }
    }

    suspend fun getChapterDetails(id: Int): Flow<ResultOf<Chapter>> = flow {
        emit(ResultOf.Loading)
        try {
            val result = bhagavadgitaApi.getChapterDetails(id)
            emit(ResultOf.Success(result))
        } catch (e: Exception) {
            emit(ResultOf.Error.Error1(e))

        }
    }

    suspend fun getVerseDetails(chapter_id: Int, verse_id: Int): Flow<ResultOf<Slok>> = flow {
        emit(ResultOf.Loading)
        try {
            val result = bhagavadgitaApi.getSlok(chapter_id, verse_id)
            emit(ResultOf.Success(result))
        } catch (e: Exception) {
            emit(ResultOf.Error.Error1(e))

        }
    }
}
