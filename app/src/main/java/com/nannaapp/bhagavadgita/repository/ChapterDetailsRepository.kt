package com.nannaapp.bhagavadgita.repository

import android.util.Log
import com.nannaapp.bhagavadgita.adapter.ChapterAdapter
import com.nannaapp.bhagavadgita.database.ChapterDao
import com.nannaapp.bhagavadgita.database.VerseDao
import com.nannaapp.bhagavadgita.model.ChapterModel
import com.nannaapp.bhagavadgita.model.cache_data.VerseInfo
import com.nannaapp.bhagavadgita.model.network_data.Slok
import com.nannaapp.bhagavadgita.network.BhagavadgitaApi
import com.nannaapp.bhagavadgita.util.ResultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class ChapterDetailsRepository
constructor(
    private val bhagavadgitaApi: BhagavadgitaApi,
    private val chapterDao: ChapterDao,
    private val verseDao: VerseDao
) {

    public final val TAG = ChapterDetailsRepository::class.java.canonicalName

    suspend fun getChapterDetails(id: Int): Flow<ResultOf<ChapterModel>> = flow {
        emit(ResultOf.Loading)
        try {
            val chapter = bhagavadgitaApi.getChapterDetails(id)
            val chapterProgress = chapterDao.getChapterProgressById(chapter.chapter_number)

            val chapterModel = ChapterModel(
                chapter.chapter_number,
                chapter.meaning,
                chapter.name,
                chapter.summary,
                chapter.translation,
                chapter.transliteration,
                chapter.verses_count,
                chapterProgress.currentReadProgress
            )
            emit(ResultOf.Success(chapterModel))
        } catch (e: Exception) {
            Log.d(TAG, "getChapterDetailsList: ${e}")
            emit(ResultOf.Error.Error1(e))

        }
    }

    suspend fun getVerseDetails(id: Int, verseCount : Int): Flow<ResultOf<List<VerseInfo>>> = flow {
        emit(ResultOf.Loading)
        try {
            var verseDetailsById = verseDao.getVerseInfoByChapterNumber(id)
            if (verseDetailsById.isEmpty()) {
                for (i in 1..verseCount) {
                    verseDao.insertVerseInfo(
                        VerseInfo(
                            verse_number = i,
                            chapter_number = id
                        )
                    )
                }
                verseDetailsById = verseDao.getVerseInfoByChapterNumber(id)
            }
                emit(ResultOf.Success(verseDetailsById))
        } catch (e: Exception) {
            Log.d(TAG, "getVerseDetails: ${e}")
            emit(ResultOf.Error.Error1(e))
        }
    }

    suspend fun updateFavoriteStatus(id: Int): Flow<ResultOf<Int>> = flow {
        emit(ResultOf.Loading)
        try {
            val status = verseDao.updateVerseFavStatus(id)
            emit(ResultOf.Success(id))
        } catch (e: Exception) {
            Log.d(TAG, "updateFavoriteStatus: ${e}")
            emit(ResultOf.Error.Error1(e))
        }
    }

}
