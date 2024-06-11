package ru.vsu.cs.simplecinemaapp.networking

import retrofit2.Call
import retrofit2.http.*
import ru.vsu.cs.simplecinemaapp.model.FullFilm
import ru.vsu.cs.simplecinemaapp.model.PageFilmsResponse


interface ApiService {

    @GET("/api/v2.2/films/{id}")
    fun getFilmById(@Path("id") id: Int = 1115471): Call<FullFilm>

    @GET("/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    fun getTopFilms(@Query("page") page: Int = 1): Call<PageFilmsResponse>

    @GET("/api/v2.1/films/search-by-keyword")
    fun findByKeyword(@Query("keyword") keyword: String, @Query("page") page: Int = 1): Call<PageFilmsResponse>
}