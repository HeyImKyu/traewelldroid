package de.hbch.traewelling.widget.models

import kotlinx.serialization.Serializable

@Serializable
data class WidgetStationsState(
    val stations: List<WidgetStation>
)

@Serializable
data class WidgetStation(
    val id: Int,
    val name: String,
    val type: String
)
