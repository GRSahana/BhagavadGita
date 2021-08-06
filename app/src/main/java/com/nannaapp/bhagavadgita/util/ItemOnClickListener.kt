package com.nannaapp.bhagavadgita.util

interface ItemOnClickListener {


    fun onVerseItemClick(chapterNumber: Int, verseNumber: Int)
    fun onFavClick(id:Int, favStatus:Boolean)
    abstract fun onItemClicked(chapterNumber: Int, versesCount: Int)
}
