package com.nannaapp.bhagavadgita.model

import com.nannaapp.bhagavadgita.model.cache_data.VerseInfo
import com.nannaapp.bhagavadgita.model.network_data.Meaning
import com.nannaapp.bhagavadgita.model.network_data.Summary

data class ChapterModel(
    val chapter_number: Int,
    val meaning: Meaning,
    val name: String,
    val summary: Summary,
    val translation: String,
    val transliteration: String,
    val verses_count: Int,
    val read_progress: Int,
    val verses_progress_list: List<VerseInfo> ?= null
)
