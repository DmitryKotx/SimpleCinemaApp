package ru.vsu.cs.simplecinemaapp.model

import com.google.gson.annotations.SerializedName

data class FullFilm(

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("nameRu")
	val nameRu: String? = null,

	@field:SerializedName("posterUrl")
	val posterUrl: String? = null,

	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@field:SerializedName("countries")
	val countries: List<CountriesItem?>? = null,

	@field:SerializedName("kinopoiskHDId")
	val kinopoiskHDId: Any? = null,

	@field:SerializedName("ratingAgeLimits")
	val ratingAgeLimits: String? = null,

	@field:SerializedName("nameEn")
	val nameEn: Any? = null,

	@field:SerializedName("shortDescription")
	val shortDescription: Any? = null,

	@field:SerializedName("ratingAwaitCount")
	val ratingAwaitCount: Int? = null,

	@field:SerializedName("logoUrl")
	val logoUrl: Any? = null,

	@field:SerializedName("coverUrl")
	val coverUrl: Any? = null,

	@field:SerializedName("nameOriginal")
	val nameOriginal: Any? = null,

	@field:SerializedName("posterUrlPreview")
	val posterUrlPreview: String? = null,

	@field:SerializedName("kinopoiskId")
	val kinopoiskId: Int? = null,

)

