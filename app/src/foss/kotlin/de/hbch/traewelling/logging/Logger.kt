package de.hbch.traewelling.logging

import android.app.Application
import de.hbch.traewelling.BuildConfig
import org.acra.config.mailSender
import org.acra.ktx.initAcra
import org.acra.ktx.sendWithAcra

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
                mailSender {
                    mailTo = BuildConfig.ACRA_REPORT_MAIL
                    subject = "[Bug report]"
                }
            }
        }
    }

    override fun captureException(t: Throwable) {
        if (needsToBeLogged(t)) {
            t.sendWithAcra()
        }
    }

    override fun captureMessage(message: String, additionalInfo: Map<String, String>) { }
}
