package com.karine.retrieve.models.geocoding

data class Response(
	val placeName: String? = null,
	val textFr: String? = null,
	val address: String? = null,
	val center: List<Double?>? = null,
	val type: String? = null,
	val placeNameFr: String? = null,
	val relevance: Int? = null,
	val placeType: List<String?>? = null,
	val context: List<ContextItem?>? = null,
	val geometry: Geometry? = null,
	val id: String? = null,
	val text: String? = null,
	val properties: Properties? = null
)
