package de.hbch.traewelling.api.models.status

import com.google.gson.annotations.SerializedName
import de.hbch.traewelling.api.models.trip.DataSource
import de.hbch.traewelling.api.models.trip.HafasOperator
import de.hbch.traewelling.api.models.trip.HafasTrainTripStation
import de.hbch.traewelling.api.models.trip.ProductType
import java.time.ZonedDateTime

data class Journey(
    @SerializedName("trip") val tripId: Int,
    @SerializedName("hafasId") val hafasTripId: String,
    val category: ProductType?,
    @SerializedName("lineName") val line: String,
    val journeyNumber: String?,
    val manualJourneyNumber: String?,
    val distance: Int,
    val points: Int,
    val duration: Int,
    val origin: HafasTrainTripStation,
    val destination: HafasTrainTripStation,
    @SerializedName("manualDeparture") val departureManual: ZonedDateTime?,
    @SerializedName("manualArrival") val arrivalManual: ZonedDateTime?,
    val operator: HafasOperator?,
    @SerializedName("number") val lineId: String,
    @SerializedName("routeColor") val lineColor: String?,
    @SerializedName("routeTextColor") val textColor: String?,
    val dataSource: DataSource?
) {
    val safeProductType get() = category ?: ProductType.UNKNOWN
}
