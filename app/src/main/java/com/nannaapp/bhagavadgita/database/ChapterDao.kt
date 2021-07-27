package com.nannaapp.bhagavadgita.database

import androidx.room.*
import com.nannaapp.bhagavadgita.model.cache_data.ChapterProgress

@Dao
interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertChapterProgress(chapterProgress: ChapterProgress) : Long

    @Query("Select * from chapter_progress")
    suspend fun getChapterProgress():List<ChapterProgress>

    @Query("Select * from chapter_progress where chapter_number = :id")
    suspend fun getChapterProgressById(id : Int): ChapterProgress

    @Query("UPDATE chapter_progress SET current_read_progress = :percentage WHERE chapter_number = :chapterNumber")
    suspend fun updateChapterProgress(chapterNumber : Int, percentage : Int):Int

}
