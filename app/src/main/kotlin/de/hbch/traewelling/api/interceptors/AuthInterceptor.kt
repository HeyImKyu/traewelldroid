package de.hbch.traewelling.api.interceptors

import de.hbch.traewelling.BuildConfig
import de.hbch.traewelling.api.AuthManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class AuthInterceptor(private val authManager: AuthManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenRef = AtomicReference<String?>(null)
        val latch = CountDownLatch(1)

        authManager.getFreshAccessToken(
            callback = { token ->
                tokenRef.set(token)
                latch.countDown()
            },
            onError = {
                latch.countDown()
            }
        )

        val ok = latch.await(10, TimeUnit.SECONDS)
        if (!ok) {
            throw IOException("Timeout while waiting for fresh access token")
        }

        val token = tokenRef.get()

        val request = chain.request().newBuilder()
            .addHeader(
                "User-Agent",
                "${BuildConfig.APPLICATION_ID}/${BuildConfig.VERSION_NAME}"
            )
            .addHeader("Accept", "application/json")
            .apply {
                if (!token.isNullOrBlank()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()

        return chain.proceed(request)
    }
}
