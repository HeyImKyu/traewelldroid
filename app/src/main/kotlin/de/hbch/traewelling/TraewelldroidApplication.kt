package de.hbch.traewelling

import android.app.Application
import com.google.android.material.color.DynamicColors
import de.hbch.traewelling.api.AuthManager
import de.hbch.traewelling.api.TraewellingApi
import de.hbch.traewelling.logging.Logger

class TraewelldroidApplication : Application() {

    lateinit var traewellingApi: TraewellingApi
        private set

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        AuthManager.getInstance(this)
        traewellingApi = TraewellingApi(this)

        Logger.getInstance().initialize(this)
    }
}
