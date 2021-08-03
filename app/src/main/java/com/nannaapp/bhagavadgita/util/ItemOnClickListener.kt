package com.nannaapp.bhagavadgita.util

interface ItemOnClickListener {


    fun onItemClick(id:Int)
    fun onFavClick(id:Int, favStatus:Boolean)
    abstract fun onItemClicked(chapterNumber: Int, versesCount: Int)
}
