package de.hbch.traewelling.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.hbch.traewelling.TraewelldroidApplication
import de.hbch.traewelling.api.models.event.Event
import java.time.ZonedDateTime

class EventViewModel(application: Application) : AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi
    suspend fun getEvents(timestamp: ZonedDateTime): List<Event> {
        return traewellingApi
                .checkInService
                .getEvents(timestamp)
                .data
    }
}
