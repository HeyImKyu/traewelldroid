package de.hbch.traewelling.shared

import androidx.lifecycle.ViewModel
import de.hbch.traewelling.api.TraewellingApi
import de.hbch.traewelling.api.models.event.Event
import java.time.ZonedDateTime

class EventViewModel : ViewModel() {
    suspend fun getEvents(timestamp: ZonedDateTime): List<Event> {
        return TraewellingApi
                .checkInService
                .getEvents(timestamp)
                .data
    }
}
