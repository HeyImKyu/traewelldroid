package de.hbch.traewelling.ui.include.status

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.hbch.traewelling.R
import de.hbch.traewelling.api.models.status.Status
import de.hbch.traewelling.ui.composables.LineIcon
import de.hbch.traewelling.ui.searchConnection.Platform
import de.hbch.traewelling.ui.user.getDurationString
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.ZonedDateTime
import kotlin.math.absoluteValue

@Composable
fun ActiveStatusBar(
    status: Status?,
    modifier: Modifier = Modifier
) {
    if (status != null) {
        var progress by remember { mutableFloatStateOf(0f) }
        val progressAnimation by animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            label = "AnimateActiveStatusBarProgress",
        )
        var duration by remember { mutableIntStateOf(0) }
        
        LaunchedEffect(true) {
            while (true) {
                progress = calculateProgress(
                    from = status.journey.departureManual ?: status.journey.origin.departureReal ?: status.journey.origin.departurePlanned,
                    to = status.journey.destination.arrivalReal ?: status.journey.destination.arrivalPlanned
                )
                duration = Duration.between(
                    status.journey.destination.arrivalReal ?: status.journey.destination.arrivalPlanned,
                    ZonedDateTime.now()
                ).toMinutes().toInt()
                delay(5000)
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = modifier.height(58.dp).padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(0.4f).padding(end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    LineIcon(
                        lineName = status.journey.line,
                        journeyNumber = null,
                        lineId = status.journey.lineId,
                        operatorCode = status.journey.operator?.id
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null
                    )
                    Text(
                        text = status.journey.destination.name,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (duration < 0) {
                        Text(
                            text = stringResource(id = R.string.time_left, getDurationString(duration.absoluteValue))
                        )
                    }
                    Platform(
                        planned = status.journey.destination.arrivalPlatformPlanned,
                        real = status.journey.destination.arrivalPlatformReal
                    )
                }
            }
            SegmentedLinearProgressIndicator(
                progress = if (progressAnimation.isNaN()) 0f else progressAnimation,
                height = 4.dp,
                margin = 8.dp
            )
        }
    }
}
