package com.nannaapp.bhagavadgita.model.cache_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapter_progress")
data class ChapterProgress(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "chapter_number", index = true)
    val chapterNumber : Int,

    @ColumnInfo(name = "current_read_progress")
    val currentReadProgress : Int
)
