package de.hbch.traewelling.ui.activeCheckins

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import de.hbch.traewelling.TraewelldroidApplication
import de.hbch.traewelling.api.models.status.Status
import de.hbch.traewelling.api.models.status.StatusPage
import de.hbch.traewelling.logging.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActiveCheckinsViewModel(application: Application) : AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi

    val isRefreshing = MutableLiveData(false)
    val checkIns = mutableStateListOf<Status>()

    init {
        getActiveCheckins()
    }

    fun getActiveCheckins() {
        isRefreshing.postValue(true)
        traewellingApi.checkInService.getStatuses()
            .enqueue(object: Callback<StatusPage> {
                override fun onResponse(call: Call<StatusPage>, response: Response<StatusPage>) {
                    isRefreshing.postValue(false)
                    if (response.isSuccessful) {
                        val statuses = response.body()
                        if (statuses != null) {
                            checkIns.clear()
                            checkIns.addAll(statuses.data)
                            return
                        }
                    }
                }
                override fun onFailure(call: Call<StatusPage>, t: Throwable) {
                    isRefreshing.postValue(false)
                    Logger.captureException(t)
                }
            })
    }
}
