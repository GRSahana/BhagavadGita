package com.nannaapp.bhagavadgita.di

import com.nannaapp.bhagavadgita.database.ChapterDao
import com.nannaapp.bhagavadgita.database.VerseDao
import com.nannaapp.bhagavadgita.network.BhagavadgitaApi
import com.nannaapp.bhagavadgita.repository.ChapterDetailsRepository
import com.nannaapp.bhagavadgita.repository.MainRepository
import com.nannaapp.bhagavadgita.repository.VerseDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        bhagavadgitaApi: BhagavadgitaApi,
        chapterDao: ChapterDao
    ): MainRepository =
        MainRepository(bhagavadgitaApi, chapterDao)

    @Provides
    @Singleton
    fun provideChapterDetailsRepository(
        bhagavadgitaApi: BhagavadgitaApi,
        chapterDao: ChapterDao,
        verseDao: VerseDao
    ): ChapterDetailsRepository =
        ChapterDetailsRepository(bhagavadgitaApi, chapterDao,verseDao)

    @Provides
    @Singleton
    fun provideVerseDetailsRepository(
        bhagavadgitaApi: BhagavadgitaApi,
        chapterDao: ChapterDao,
        verseDao: VerseDao
    ): VerseDetailsRepository =
        VerseDetailsRepository(bhagavadgitaApi, chapterDao,verseDao)
}
