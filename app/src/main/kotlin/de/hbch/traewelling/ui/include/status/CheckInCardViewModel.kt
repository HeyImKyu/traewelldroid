package de.hbch.traewelling.ui.include.status

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.hbch.traewelling.TraewelldroidApplication
import de.hbch.traewelling.logging.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckInCardViewModel(application: Application) : AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi

    fun createFavorite(statusId: Int, successCallback: () -> Unit) {
        traewellingApi.checkInService.createFavorite(statusId)
            .enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        successCallback()
                        return
                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Logger.captureException(t)
                }
            })
    }

    fun deleteFavorite(statusId: Int, successCallback: () -> Unit) {
        traewellingApi.checkInService.deleteFavorite(statusId)
            .enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
                        successCallback()
                        return
                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Logger.captureException(t)
                }
            })
    }

    fun deleteStatus(
        statusId: Int,
        successCallback: () -> Unit,
        failureCallback: () -> Unit
    ) {
        traewellingApi.checkInService.deleteStatus(statusId)
            .enqueue(object: Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        successCallback()
                        return
                    }
                    failureCallback()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    failureCallback()
                    Logger.captureException(t)
                }
            })
    }
}
