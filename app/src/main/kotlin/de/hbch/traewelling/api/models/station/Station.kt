package de.hbch.traewelling.api.models.station

import com.google.gson.annotations.SerializedName

data class Station(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerializedName("ibnr") val evaIdentifier: Long?,
    @SerializedName("rilIdentifier") val ds100: String?,
    val areas: List<Area>?
)

data class Area(
    val name: String,
    val adminLevel: Int,
    val default: Boolean
)
