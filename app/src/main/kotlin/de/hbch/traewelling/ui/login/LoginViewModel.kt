package de.hbch.traewelling.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.hbch.traewelling.api.WebhookRelayApi
import de.hbch.traewelling.api.models.webhook.WebhookUserCreateRequest
import de.hbch.traewelling.logging.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    fun createWebhookUser(
        webhookUserCreateRequest: WebhookUserCreateRequest,
        successfulCallback: (String) -> Unit,
        failureUrlCallback: () -> Unit
    ) {
        WebhookRelayApi.service
            .createWebhookUser(webhookUserCreateRequest)
            .enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val id = response.body()
                        if (id != null) {
                            successfulCallback(id)
                            return
                        }
                    }
                    failureUrlCallback()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Logger.captureException(t)
                }
            })
    }
}
