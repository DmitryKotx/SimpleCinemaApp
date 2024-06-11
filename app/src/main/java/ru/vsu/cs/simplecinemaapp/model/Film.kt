package ru.vsu.cs.simplecinemaapp.model

import com.google.gson.annotations.SerializedName

data class Film(

	@field:SerializedName("year")
	val year: String? = null,

	@field:SerializedName("nameEn")
	val nameEn: String? = null,

	@field:SerializedName("nameRu")
	val nameRu: String? = null,

	@field:SerializedName("posterUrl")
	val posterUrl: String? = null,

	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@field:SerializedName("filmId")
	val filmId: Int? = null,

	@field:SerializedName("posterUrlPreview")
	val posterUrlPreview: String? = null
)

data class CountriesItem(

	@field:SerializedName("country")
	val country: String? = null
)

data class GenresItem(

	@field:SerializedName("genre")
	val genre: String? = null
)
