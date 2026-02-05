package de.hbch.traewelling.ui.dashboard

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

class DashboardFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi

    val checkIns = mutableStateListOf<Status>()
    var isRefreshing = MutableLiveData(false)

    init {
        loadCheckIns(1)
    }

    fun loadCheckIns(
        page: Int
    ) {
        isRefreshing.postValue(true)
        traewellingApi
            .checkInService
            .getPersonalDashboard(page)
            .enqueue(object: Callback<StatusPage> {
                override fun onResponse(call: Call<StatusPage>, response: Response<StatusPage>) {
                    isRefreshing.postValue(false)
                    if (response.isSuccessful) {
                        val statusPage = response.body()
                        if (statusPage != null) {
                            checkIns.addAll(statusPage.data.filter { status -> checkIns.find { status.id == it.id } == null })
                        }
                        return
                    }
                }
                override fun onFailure(call: Call<StatusPage>, t: Throwable) {
                    isRefreshing.postValue(false)
                    Logger.captureException(t)
                }
            })
    }

    fun refresh() {
        checkIns.clear()
        loadCheckIns(1)
    }
}
