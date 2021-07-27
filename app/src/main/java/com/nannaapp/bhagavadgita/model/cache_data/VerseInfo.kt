package com.nannaapp.bhagavadgita.model.cache_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "verse_info", foreignKeys = [ForeignKey(
        entity = ChapterProgress::class,
        parentColumns = arrayOf("chapter_number"),
        childColumns = arrayOf("chapter_number"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class VerseInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "verse_number")
    val verse_number: Int,

    @ColumnInfo(name = "chapter_number" )
    val chapter_number: Int,

    @ColumnInfo(name = "favorite")
    val favorite: Boolean = false,

    @ColumnInfo(name = "read_status")
    val read_status: Boolean = false
)
