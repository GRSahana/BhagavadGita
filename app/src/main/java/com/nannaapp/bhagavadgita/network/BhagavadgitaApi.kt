package com.nannaapp.bhagavadgita.network

import com.nannaapp.bhagavadgita.model.network_data.Chapter
import com.nannaapp.bhagavadgita.model.network_data.Slok
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface BhagavadgitaApi {

    companion object {
        const val BASE_URL = "https://bhagavadgitaapi.in/"
        const val CLIENT_ACCESS_KEY = "1e2939c0d12d42a8d"
    }

    @Headers(
        "x-api-key: $CLIENT_ACCESS_KEY"
    )
    @GET("slok/{ch}/{sl}")
    suspend fun getSlok(
        @Path("ch") chapterNumber: Int,
        @Path("sl") slokNumber: Int
    ): Slok

    @Headers(
        "x-api-key: $CLIENT_ACCESS_KEY"
    )
    @GET("chapters")
    suspend fun getChaptersList(): List<Chapter>

    @Headers(
        "x-api-key: $CLIENT_ACCESS_KEY"
    )
    @GET("chapter/{ch}")
    suspend fun getChapterDetails(
        @Path("ch") chapterNumber: Int
    ): Chapter

    @Headers(
        "x-api-key: $CLIENT_ACCESS_KEY"
    )
    @GET("gita.svg")
    suspend fun searchPhotos(
        @Query("ch") chapterNumber: String,
        @Query("sl") slokNumber: String
    ): Slok

}
