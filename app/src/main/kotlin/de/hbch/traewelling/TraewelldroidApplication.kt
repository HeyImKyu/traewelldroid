package de.hbch.traewelling

import android.app.Application
import com.google.android.material.color.DynamicColors
import de.hbch.traewelling.logging.Logger

class TraewelldroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        Logger.getInstance().initialize(this)
    }
}
