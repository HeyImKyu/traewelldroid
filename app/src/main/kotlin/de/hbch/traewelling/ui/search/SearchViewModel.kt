package de.hbch.traewelling.ui.search

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import de.hbch.traewelling.TraewelldroidApplication
import de.hbch.traewelling.api.models.station.Station
import de.hbch.traewelling.api.models.user.User

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi

    suspend fun searchUsers(
        query: String,
        page: Int = 1
    ): List<User>? {
        return try {
            traewellingApi.userService.searchUsers(query, page).data
        } catch (_: Exception) {
            null
        }
    }

    suspend fun searchStations(
        query: String
    ): List<Station>? {
        return try {
            val stations = traewellingApi.travelService.autoCompleteStationSearch(query).data
            stations.sortedWith(compareBy(nullsLast()) { it.ds100 })
        } catch (_: Exception) {
            null
        }
    }

    suspend fun searchNearbyStation(
        location: Location
    ): Station? {
        return try {
            traewellingApi.travelService.getNearbyStation(location.latitude, location.longitude).data
        } catch (_: Exception) {
            null
        }
    }
}
