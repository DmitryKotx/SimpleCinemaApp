package ru.vsu.cs.simplecinemaapp.model

import com.google.gson.annotations.SerializedName

data class PageFilmsResponse(

	@field:SerializedName("films")
	val films: List<Film>? = null,

	@field:SerializedName("pagesCount")
	val pagesCount: Int? = null
)

