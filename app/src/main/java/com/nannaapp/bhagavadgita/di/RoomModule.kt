package com.nannaapp.bhagavadgita.di

import android.content.Context
import androidx.room.Room
import com.nannaapp.bhagavadgita.database.ChapterDao
import com.nannaapp.bhagavadgita.database.ReadProgressDatabase
import com.nannaapp.bhagavadgita.database.VerseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideReadProgressDb(@ApplicationContext context: Context): ReadProgressDatabase {
        return Room.databaseBuilder(
            context,
            ReadProgressDatabase::class.java,
            ReadProgressDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideChapterDAO(readProgressDatabase: ReadProgressDatabase): ChapterDao {
        return readProgressDatabase
            .chapterDao()
    }

    @Singleton
    @Provides
    fun provideVerseDAO(readProgressDatabase: ReadProgressDatabase): VerseDao {
        return readProgressDatabase
            .verseDao()
    }
}
