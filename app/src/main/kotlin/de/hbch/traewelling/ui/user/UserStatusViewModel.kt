package de.hbch.traewelling.ui.user

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.hbch.traewelling.api.TraewellingApi
import de.hbch.traewelling.api.models.Data
import de.hbch.traewelling.api.models.status.Status
import de.hbch.traewelling.api.models.status.StatusPage
import de.hbch.traewelling.api.models.user.User
import de.hbch.traewelling.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserStatusViewModel : ViewModel() {

    val checkIns = mutableStateListOf<Status>()
    val user = MutableLiveData<User?>(null)
    var isRefreshing = MutableLiveData(false)

    fun loadUser(
        username: String?,
        searchRequired: Boolean = false
    ) {
        if (username != null) {
            isRefreshing.postValue(true)
            if (searchRequired) {
                searchForUser(username)
            } else {
                TraewellingApi.userService.getUser(username)
                    .enqueue(object : Callback<Data<User>> {
                        override fun onResponse(
                            call: Call<Data<User>>,
                            response: Response<Data<User>>
                        ) {
                            isRefreshing.postValue(false)
                            if (response.isSuccessful) {
                                val respUser = response.body()
                                if (respUser != null) {
                                    user.postValue(respUser.data)
                                    resetStatusesForUser(respUser.data.username)
                                }
                                return
                            } else if (response.code() == 403) {
                                searchForUser(username)
                            }
                        }

                        override fun onFailure(call: Call<Data<User>>, t: Throwable) {
                            isRefreshing.postValue(false)
                            Logger.captureException(t)
                        }
                    })
            }
        }
    }

    fun searchForUser(username: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Main.immediate) {
                try {
                    val respUsers =
                        TraewellingApi.userService.searchUsers(username).data
                    if (respUsers.isNotEmpty() && respUsers[0].username == username) {
                        user.postValue(respUsers[0])
                    }
                } catch (e: Exception) {
                    Logger.captureException(e)
                } finally {
                    isRefreshing.postValue(false)
                }
            }
        }
    }

    fun loadStatusesForUser(
        username: String = user.value?.username ?: "",
        page: Int = 1
    ) {
        isRefreshing.postValue(true)
        TraewellingApi.checkInService.getStatusesForUser(username, page)
            .enqueue(object : Callback<StatusPage> {
                override fun onResponse(
                    call: Call<StatusPage>,
                    response: Response<StatusPage>
                ) {
                    isRefreshing.postValue(false)
                    if (response.isSuccessful) {
                        val statusPage = response.body()
                        if (statusPage != null) {
                            checkIns.addAll(statusPage.data)
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

    fun resetStatusesForUser(
        username: String? = user.value?.username ?: "",
    ) {
        if (username != null) {
            checkIns.clear()
            loadStatusesForUser(username, 1)
        }
    }

    fun handleFollowButton() {
        user.value?.let { userValue ->
            if (userValue.following)
                unfollowUser(userValue.id)
            else
                followUser(userValue.id)
        }
    }

    fun handleMuteButton() {
        user.value?.let { userValue ->
            if (userValue.muted)
                unmuteUser(userValue.id)
            else
                muteUser(userValue.id)
        }
    }

    private fun followUser(userId: Int) {
        TraewellingApi.userService.followUser(userId)
            .enqueue(object: Callback<Data<Unit>> {
                override fun onResponse(call: Call<Data<Unit>>, response: Response<Data<Unit>>) {
                    if (response.isSuccessful) {
                        user.postValue(user.value?.let {
                            val hasPrivateProfile = it.privateProfile
                            it.copy(following = !hasPrivateProfile, followRequestPending = hasPrivateProfile)
                        })
                        return
                    }
                }

                override fun onFailure(call: Call<Data<Unit>>, t: Throwable) {
                    Logger.captureException(t)
                }
            })
    }

    private fun unfollowUser(userId: Int) {
        TraewellingApi.userService.unfollowUser(userId)
            .enqueue(object: Callback<Data<Unit>> {
                override fun onResponse(call: Call<Data<Unit>>, response: Response<Data<Unit>>) {
                    if (response.isSuccessful) {
                        user.postValue(user.value?.copy(following = false))
                        return
                    }
                }

                override fun onFailure(call: Call<Data<Unit>>, t: Throwable) {
                    Logger.captureException(t)
                }
            })
    }

    private fun muteUser(userId: Int) {
        TraewellingApi.userService.muteUser(userId)
            .enqueue(object: Callback<Data<Unit>> {
                override fun onResponse(call: Call<Data<Unit>>, response: Response<Data<Unit>>) {
                    if (response.isSuccessful) {
                        user.postValue(user.value?.copy(muted = true))
                        return
                    }
                }

                override fun onFailure(call: Call<Data<Unit>>, t: Throwable) {
                    Logger.captureException(t)
                }
            })
    }

    private fun unmuteUser(userId: Int) {
        TraewellingApi.userService.unmuteUser(userId)
            .enqueue(object: Callback<Data<Unit>> {
                override fun onResponse(call: Call<Data<Unit>>, response: Response<Data<Unit>>) {
                    if (response.isSuccessful) {
                        user.postValue(user.value?.copy(muted = false))
                        return
                    }
                }

                override fun onFailure(call: Call<Data<Unit>>, t: Throwable) {
                    Logger.captureException(t)
                }
            })
    }
}
