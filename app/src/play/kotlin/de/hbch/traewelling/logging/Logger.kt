package de.hbch.traewelling.logging

import android.app.Application
import org.acra.data.StringFormat
import org.acra.ktx.initAcra
import de.hbch.traewelling.BuildConfig
import org.acra.ACRA
import org.acra.config.httpSender
import org.acra.ktx.sendSilentlyWithAcra
import org.acra.sender.HttpSender

class Logger private constructor(): ILogger {

    companion object {
        @Volatile
        private var instance: Logger? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Logger().also {
                instance = it
            }
        }

        fun captureException(t: Throwable) {
            getInstance().captureException(t)
        }

        fun captureMessage(message: String, additionalInfo: Map<String, String>) {
            getInstance().captureMessage(message, additionalInfo)
        }
    }

    override fun initialize(application: Application) {
        if (BuildConfig.ENABLE_ACRA) {
            application.initAcra {
                buildConfigClass = BuildConfig::class.java
                reportFormat = StringFormat.JSON

                httpSender {
                    uri = BuildConfig.ACRA_ENDPOINT
                    basicAuthLogin = BuildConfig.ACRA_USERNAME
                    basicAuthPassword = BuildConfig.ACRA_PASSWORD
                    httpMethod = HttpSender.Method.POST
                }
            }
        }
    }

    override fun captureException(t: Throwable) {
        if (needsToBeLogged(t)) {
            t.sendSilentlyWithAcra()
        }
    }

    override fun captureMessage(message: String, additionalInfo: Map<String, String>) {
        additionalInfo.forEach {
            ACRA.errorReporter.putCustomData(it.key, it.value)
        }
        Exception(message).sendSilentlyWithAcra()
    }
}
