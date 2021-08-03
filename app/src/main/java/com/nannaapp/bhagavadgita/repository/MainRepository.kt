package com.nannaapp.bhagavadgita.repository

import android.util.Log
import com.nannaapp.bhagavadgita.adapter.ChapterAdapter
import com.nannaapp.bhagavadgita.database.ChapterDao
import com.nannaapp.bhagavadgita.model.ChapterModel
import com.nannaapp.bhagavadgita.model.cache_data.ChapterProgress
import com.nannaapp.bhagavadgita.model.network_data.Chapter
import com.nannaapp.bhagavadgita.model.network_data.Slok
import com.nannaapp.bhagavadgita.network.BhagavadgitaApi
import com.nannaapp.bhagavadgita.util.ResultOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class MainRepository
constructor(
    private val bhagavadgitaApi: BhagavadgitaApi,
    private val chapterDao : ChapterDao
) {
    public final val TAG = MainRepository::class.java.canonicalName

    suspend fun getChapterList(): Flow<ResultOf<List<ChapterModel>>> = flow {
        emit(ResultOf.Loading)
        try {
            val result = bhagavadgitaApi.getChaptersList()
            var chapterProgressList = chapterDao.getChapterProgress()
            Log.d(TAG, "getChapterList: ${result.toString()}")
            Log.d(TAG, "getChapterList: ${chapterProgressList.toString()}")
            if(chapterProgressList.isEmpty()) {
                for (c in result) {
                    val chapterProgress = ChapterProgress(c.chapter_number, 0)
                    chapterDao.insertChapterProgress(chapterProgress)
                }
                chapterProgressList = chapterDao.getChapterProgress()
                Log.d(TAG, "getChapterList: ${chapterProgressList.toString()}")
            }
            val chapterModelList: MutableList<ChapterModel> = mutableListOf<ChapterModel>()
            for(i in 0..17){
                val chapterModel = ChapterModel(
                    result[i].chapter_number,
                    result[i].meaning,
                    result[i].name,
                    result[i].summary,
                    result[i].translation,
                    result[i].transliteration,
                    result[i].verses_count,
                    chapterProgressList[i].currentReadProgress
                )
                chapterModelList.add(chapterModel)
            }
            emit(ResultOf.Success(chapterModelList))
        } catch (e: Exception) {
            Log.d(TAG, "getChapterList: ${e}")
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
