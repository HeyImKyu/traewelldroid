package de.hbch.traewelling.logging

import android.app.Application
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kotlin.coroutines.cancellation.CancellationException

interface ILogger {
    val ignoredExceptions get() = listOf(
        SocketException::class,
        SocketTimeoutException::class,
        UnknownHostException::class,
        ConnectException::class,
        SSLHandshakeException::class,
        CancellationException::class
    )

    fun initialize(application: Application)
    fun captureException(t: Throwable)
    fun captureMessage(message: String, additionalInfo: Map<String, String>)
    fun needsToBeLogged(t: Throwable) = ignoredExceptions.none { it.isInstance(t) }
}
