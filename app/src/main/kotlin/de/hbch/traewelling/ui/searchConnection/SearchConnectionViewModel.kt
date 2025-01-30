package de.hbch.traewelling.ui.searchConnection

import androidx.lifecycle.ViewModel
import de.hbch.traewelling.api.TraewellingApi
import de.hbch.traewelling.api.models.station.Station
import de.hbch.traewelling.api.models.trip.HafasTripPage
import java.time.ZonedDateTime

class SearchConnectionViewModel: ViewModel() {

    suspend fun searchConnections(
        stationId: Int,
        departureTime: ZonedDateTime,
        filterType: FilterType?
    ): Triple<Int, HafasTripPage?, Exception?> {
        return try {
            val tripPage = TraewellingApi
                .travelService
                .getDeparturesAtStation(
                    stationId,
                    departureTime,
                    filterType?.filterQuery ?: ""
                )

            Triple(tripPage.code(), tripPage.body(), Exception(tripPage.errorBody()?.string() ?: ""))
        } catch (ex: Exception) {
            return Triple(500, null, ex)
        }
    }

    suspend fun setUserHomelandStation(
        stationId: Int
    ): Station? {
        return try {
            TraewellingApi.authService.setUserHomelandStation(stationId).data
        } catch (_: Exception) {
            null
        }
    }
}
