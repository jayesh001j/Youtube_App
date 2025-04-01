package com.example.youtubeapp.api
import YoutubeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("youtube/v3/search")
    suspend fun getMostPopularVideos(
        @Query("key") apiKey: String = "AIzaSyAs1aFi4ZH8uhJn20cfM4HuDZu28oySAa8",
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("q") query: String = "",
        @Query("maxResults") maxResults: Int = 20
    ): Response<YoutubeResponse>
}
