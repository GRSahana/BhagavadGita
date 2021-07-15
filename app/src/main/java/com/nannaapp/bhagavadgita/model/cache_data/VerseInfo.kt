package com.nannaapp.bhagavadgita.model.cache_data

data class VerseInfo(
    val verse_number: Int,
    val chapter_number : Int = 1,
    val read_status : Boolean = false,
    val favorite : Boolean = false
    )
