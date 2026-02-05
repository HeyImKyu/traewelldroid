package de.hbch.traewelling.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import de.hbch.traewelling.shared.SettingsViewModel
import de.hbch.traewelling.theme.LineIconStyle
import de.hbch.traewelling.theme.LocalFont
import de.hbch.traewelling.util.colorFromHex

private fun getContrastTextColor(background: Color): Color {
    return if (background.luminance() > 0.5f) Color.Black else Color.White
}

@Composable
fun LineIcon(
    lineName: String,
    journeyNumber: String?,
    modifier: Modifier = Modifier,
    lineColorString: String? = null,
    textColorString: String? = null,
    defaultTextStyle: TextStyle = LocalFont.current.bodyMedium,
    displayJourneyNumber: Boolean = true
) {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = context as ViewModelStoreOwner
    )
    val displayJourneyNumberSetting by settingsViewModel.displayJourneyNumber.observeAsState(true)

    val displayedName = lineName.split(" (").firstOrNull() ?: lineName

    val lineColor: Color? = try {
        colorFromHex("#$lineColorString")
    } catch (_: Exception) {
        null
    }

    val textColor: Color? = try {
        colorFromHex("#$textColorString")
    } catch (_: Exception) {
        null
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (lineColor != null) {
            Box(
                modifier = Modifier
                    .widthIn(48.dp, 144.dp)
                    .background(
                        color = lineColor,
                        shape = RoundedCornerShape(percent = 20)
                    )
                    .padding(2.dp)
            ) {
                Text(
                    text = displayedName,
                    modifier = Modifier.align(Alignment.Center),
                    color = textColor ?: getContrastTextColor(lineColor),
                    style = LineIconStyle,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Text(
                text = displayedName,
                modifier = modifier,
                style = defaultTextStyle,
                fontWeight = FontWeight.ExtraBold
            )
        }
        if (displayJourneyNumber
            && displayJourneyNumberSetting
            && !displayedName.contains(journeyNumber ?: "")
            && journeyNumber != null && journeyNumber != ""
            && journeyNumber.length <= 8
        ) {
            Text(
                text = "($journeyNumber)",
                style = LocalFont.current.bodySmall
            )
        }
    }
}

@Composable
fun LineIconView(
    lineName: String,
    modifier: Modifier = Modifier,
    lineColorString: String? = null,
    textColorString: String? = null,
    defaultTextStyle: TextStyle = LocalFont.current.bodyMedium
) {
    val displayedName = lineName.split(" (").firstOrNull() ?: lineName

    val lineColor: Color? = try {
        colorFromHex("#$lineColorString")
    } catch (_: Exception) {
        null
    }

    val textColor: Color? = try {
        colorFromHex("#$textColorString")
    } catch (_: Exception) {
        null
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (lineColor != null) {
            Box(
                modifier = Modifier
                    .widthIn(48.dp, 144.dp)
                    .background(
                        color = lineColor,
                        shape = RoundedCornerShape(percent = 20)
                    )
                    .padding(2.dp)
            ) {
                Text(
                    text = displayedName,
                    modifier = Modifier.align(Alignment.Center),
                    color = textColor ?: getContrastTextColor(lineColor),
                    style = LineIconStyle,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Text(
                text = displayedName,
                modifier = modifier,
                style = defaultTextStyle,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
