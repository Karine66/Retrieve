package com.karine.retrieve.models.Geocoding

data class Response(
	val features: List<FeaturesItem?>? = null,
	val query: List<String?>? = null,
	val attribution: String? = null,
	val type: String? = null
)
