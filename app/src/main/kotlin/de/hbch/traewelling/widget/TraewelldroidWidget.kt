package de.hbch.traewelling.widget

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.material3.ColorProviders
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.jcloquell.androidsecurestorage.SecureStorage
import de.hbch.traewelling.R
import de.hbch.traewelling.shared.SharedValues.SS_WIDGET_STATIONS_STATE
import de.hbch.traewelling.theme.DarkColorScheme
import de.hbch.traewelling.theme.LightColorScheme
import de.hbch.traewelling.ui.launcher.LauncherActivity
import de.hbch.traewelling.util.TraewelldroidUriBuilder
import de.hbch.traewelling.widget.models.WidgetStationsState
import kotlinx.serialization.json.Json

class TraewelldroidWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val secureStorage = SecureStorage(context)
            val stationsState = secureStorage.getObject(SS_WIDGET_STATIONS_STATE, String::class.java)
            val stations = try {
                stationsState?.let {
                    Json.decodeFromString<WidgetStationsState>(stationsState)
                }
            } catch (_: Exception) {
                null
            }
            GlanceTheme(
                colors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    GlanceTheme.colors
                } else {
                    ColorProviders(
                        light = LightColorScheme,
                        dark = DarkColorScheme
                    )
                }
            ) {
                Scaffold(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .cornerRadius(16.dp)
                        .clickable(actionStartActivity<LauncherActivity>())
                ) {
                    MainWidget(
                        stations = stations
                    )
                }
            }
        }
    }

    @Composable
    private fun MainWidget(
        stations: WidgetStationsState? = null
    ) {
        Row(
            modifier = GlanceModifier.padding(vertical = 6.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                provider = ImageProvider(R.drawable.ic_logo),
                contentDescription = null,
                colorFilter = ColorFilter.tint(GlanceTheme.colors.primary),
                modifier = GlanceModifier.size(32.dp)
            )
            if (stations != null) {
                Column(
                    modifier = GlanceModifier.fillMaxHeight().defaultWeight().padding(start = 12.dp)
                ) {
                    stations.stations.forEachIndexed { index, station ->
                        val icon = if (station.type == "home") R.drawable.ic_home else R.drawable.ic_history
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            TraewelldroidUriBuilder()
                                .appendPath("trains")
                                .appendPath("stationboard")
                                .appendQueryParameter("station", station.id.toString())
                                .build()
                        )
                        Row(
                            modifier = GlanceModifier
                                .fillMaxWidth()
                                .defaultWeight()
                                .padding(6.dp)
                                .background(GlanceTheme.colors.primaryContainer)
                                .cornerRadius(16.dp)
                                .clickable(androidx.glance.appwidget.action.actionStartActivity(intent)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                provider = ImageProvider(icon),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(GlanceTheme.colors.onPrimaryContainer)
                            )
                            Text(
                                text = station.name,
                                maxLines = 1,
                                modifier = GlanceModifier.padding(start = 6.dp),
                                style = TextStyle(color = GlanceTheme.colors.onPrimaryContainer)
                            )
                        }
                        if (index == 0) {
                            Spacer(
                                modifier = GlanceModifier.height(6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}