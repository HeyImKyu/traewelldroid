package de.hbch.traewelling.api.models.trip

import de.hbch.traewelling.api.models.meta.Times
import de.hbch.traewelling.api.models.station.Station

data class HafasMeta(
    val times: Times,
    val station: Station,
    val removedCount: Int
)
