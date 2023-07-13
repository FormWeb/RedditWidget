package be.bxl.moorluck.exoredditwidget.api

import be.bxl.moorluck.exoredditwidget.api.dto.Meme
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface MemeApi {
    
    @GET("gimme/{subreddit}")
    fun randomMeme(@Path("subreddit") subreddit : String): Call<Meme>
}