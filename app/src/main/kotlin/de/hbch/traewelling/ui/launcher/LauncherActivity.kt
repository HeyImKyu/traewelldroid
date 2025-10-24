package de.hbch.traewelling.ui.launcher

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jcloquell.androidsecurestorage.SecureStorage
import de.hbch.traewelling.api.models.notifications.NotificationChannelType
import de.hbch.traewelling.shared.MastodonEmojis
import de.hbch.traewelling.shared.SharedValues
import de.hbch.traewelling.ui.login.LoginActivity
import de.hbch.traewelling.ui.main.MainActivity
import de.hbch.traewelling.util.refreshJwt

class LauncherActivity : AppCompatActivity() {
    private var splashScreenVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition(object: SplashScreen.KeepOnScreenCondition {
            override fun shouldKeepOnScreen(): Boolean {
                return splashScreenVisible
            }
        })

        /*val icons = runBlocking {
            readOrDownloadLineIcons()
        }
        LineIcons.getInstance().icons.addAll(icons)*/
        MastodonEmojis.getInstance(this)

        createNotificationChannels()

        val secureStorage = SecureStorage(this)
        val jwt = secureStorage.getObject(SharedValues.SS_JWT, String::class.java)
        if (jwt == null) {
            start(LoginActivity::class.java)
        } else {
            val startMain: () -> Unit = {
                start(MainActivity::class.java)
            }
            refreshJwt(
                onTokenReceived = { startMain() },
                onError = startMain
            )
        }
        super.onCreate(savedInstanceState)
    }

    private fun start(cls: Class<out Activity>) {
        splashScreenVisible = false
        val startupIntent = Intent(this, cls)
        startActivity(startupIntent)
        finish()
    }

    private fun createNotificationChannels() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        if (notificationManager != null) {
            val channels = NotificationChannelType.entries.map { channel ->
                val channelName = getString(channel.title)
                val channelDescription = getString(channel.description)

                val notificationChannel = NotificationChannel(
                    channel.name,
                    channelName,
                    channel.importance
                )
                notificationChannel.description = channelDescription
                return@map notificationChannel
            }
            notificationManager.createNotificationChannels(channels)
        }
    }
}
