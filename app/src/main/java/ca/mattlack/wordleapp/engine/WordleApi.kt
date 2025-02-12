package ca.mattlack.wordleapp.engine

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WordleApi {
    @GET("/word/{day}")
    suspend fun getWord(@Path("day") day: Int): Response<String>
    @GET("/guess")
    suspend fun guessWord(@Query("guess") guess: String, @Query("word") word: String): Response<GuessResponse>
    @GET("/word-count")
    suspend fun numDays(): Response<Int>
}