package com.nannaapp.bhagavadgita.repository

import android.util.Log
import com.nannaapp.bhagavadgita.adapter.ChapterAdapter
import com.nannaapp.bhagavadgita.database.ChapterDao
import com.nannaapp.bhagavadgita.database.VerseDao
import com.nannaapp.bhagavadgita.model.ChapterModel
import com.nannaapp.bhagavadgita.model.cache_data.VerseInfo
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
            var verseDetailsById = verseDao.getVerseInfoByChapterNumber(chapter.chapter_number)
            if (verseDetailsById.isEmpty()) {
                for (i in 1..chapter.verses_count) {
                    verseDao.insertVerseInfo(
                        VerseInfo(
                            verse_number = i,
                            chapter_number = chapter.chapter_number
                        )
                    )
                }
                verseDetailsById = verseDao.getVerseInfoByChapterNumber(chapter.chapter_number)
            }
            val chapterModel = ChapterModel(
                chapter.chapter_number,
                chapter.meaning,
                chapter.name,
                chapter.summary,
                chapter.translation,
                chapter.transliteration,
                chapter.verses_count,
                chapterProgress.currentReadProgress,
                verseDetailsById
            )
            emit(ResultOf.Success(chapterModel))
        } catch (e: Exception) {
            Log.d(TAG, "getChapterDetailsList: ${e}")
            emit(ResultOf.Error.Error1(e))

        }
    }

}
