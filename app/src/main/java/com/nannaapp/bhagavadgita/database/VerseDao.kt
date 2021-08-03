package com.nannaapp.bhagavadgita.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nannaapp.bhagavadgita.model.cache_data.VerseInfo

@Dao
interface VerseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertVerseInfo(verseInfo: VerseInfo) : Long

    @Query("Select * from verse_info where chapter_number = :chapterNumber")
    suspend fun getVerseInfoByChapterNumber(chapterNumber:Int):List<VerseInfo>

    @Query("UPDATE verse_info SET favorite = not favorite WHERE id = :id")
    suspend fun updateVerseFavStatus(id : Int):Int

    @Query("UPDATE verse_info SET read_status = :readStatus WHERE id = :id")
    suspend fun updateVerseReadStatus(id : Int, readStatus : Boolean):Int

}
