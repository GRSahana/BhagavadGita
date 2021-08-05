package com.nannaapp.bhagavadgita.repository

import android.util.Log
import com.nannaapp.bhagavadgita.database.ChapterDao
import com.nannaapp.bhagavadgita.database.VerseDao
import com.nannaapp.bhagavadgita.model.network_data.Slok
import com.nannaapp.bhagavadgita.network.BhagavadgitaApi
import com.nannaapp.bhagavadgita.util.ResultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class VerseDetailsRepository
constructor(
    private val bhagavadgitaApi: BhagavadgitaApi,
    private val chapterDao: ChapterDao,
    private val verseDao: VerseDao
)  {

    public final val TAG = VerseDetailsRepository::class.java.canonicalName

    suspend fun getVerseDetails(chapter_id: Int, verse_id: Int): Flow<ResultOf<Slok>> = flow {
        emit(ResultOf.Loading)
        try {
            val result = bhagavadgitaApi.getSlok(chapter_id, verse_id)
            emit(ResultOf.Success(result))
        } catch (e: Exception) {
            Log.d(TAG, "getVerseDetails: ${e}")
            emit(ResultOf.Error.Error1(e))

        }
    }

    suspend fun updateReadStatus(chapterNumber: Int, verseNumber: Int): Flow<ResultOf<Int>> = flow {
        emit(ResultOf.Loading)
        try {
            verseDao.updateVerseReadStatus(verseNumber,chapterNumber, true)
            val verInfoRead = verseDao.getVerseInfoByChapterNumberRead(chapterNumber)
            Log.d(TAG, "updateReadStatus: ${verInfoRead.size}")
            chapterDao.updateChapterProgress(chapterNumber, verInfoRead.size)
            emit(ResultOf.Success(verseNumber))
        } catch (e: Exception) {
            Log.d(TAG, "updateReadStatus: ${e}")
            emit(ResultOf.Error.Error1(e))
        }
    }

}