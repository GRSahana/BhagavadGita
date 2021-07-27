package com.nannaapp.bhagavadgita.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nannaapp.bhagavadgita.model.cache_data.ChapterProgress
import com.nannaapp.bhagavadgita.model.cache_data.VerseInfo

@Database(entities = [ChapterProgress::class, VerseInfo::class], version = 1)
abstract class ReadProgressDatabase : RoomDatabase(){

    abstract fun chapterDao() : ChapterDao

    abstract fun verseDao() : VerseDao

    companion object{
        val DATABASE_NAME = "read_progress_db"
    }
}
