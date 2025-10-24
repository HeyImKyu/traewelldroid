package de.hbch.traewelling.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import com.jcloquell.androidsecurestorage.SecureStorage
import de.hbch.traewelling.api.models.station.Station
import de.hbch.traewelling.shared.SharedValues.SS_WIDGET_STATIONS_STATE
import de.hbch.traewelling.widget.models.WidgetStation
import de.hbch.traewelling.widget.models.WidgetStationsState
import kotlinx.serialization.json.Json

suspend fun Context.updateWidgetState(homelandStation: Station?, lastVisitedStations: List<Station>?) {
    val widgetManager = GlanceAppWidgetManager(this)
    val widget = TraewelldroidWidget()
    val glanceIds = widgetManager.getGlanceIds(widget.javaClass)
    if (glanceIds.isNotEmpty()) {
        val stations = mutableListOf<WidgetStation>()
        homelandStation?.let {
            stations.add(WidgetStation(it.id, it.name, "home"))
        }
        lastVisitedStations?.firstOrNull { s -> s.id != homelandStation?.id }?.let {
            stations.add(WidgetStation(it.id, it.name, "recent"))
        }
        val state = WidgetStationsState(stations)
        val secureStorage = SecureStorage(this)
        val json = Json.encodeToString(state)
        secureStorage.storeObject(SS_WIDGET_STATIONS_STATE, json)
        widget.updateAll(this)
    }
}
