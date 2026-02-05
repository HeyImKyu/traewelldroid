package de.hbch.traewelling.ui.searchConnection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.hbch.traewelling.TraewelldroidApplication
import de.hbch.traewelling.api.models.station.Station
import de.hbch.traewelling.api.models.trip.HafasTripPage
import java.time.ZonedDateTime

class SearchConnectionViewModel(application: Application): AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi

    suspend fun searchConnections(
        stationId: Int,
        departureTime: ZonedDateTime,
        filterType: FilterType?
    ): Triple<Int, HafasTripPage?, Exception?> {
        return try {
            val tripPage = traewellingApi
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
            traewellingApi.authService.setUserHomelandStation(stationId).data
        } catch (_: Exception) {
            null
        }
    }
}
