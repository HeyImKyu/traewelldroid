package de.hbch.traewelling.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.hbch.traewelling.TraewelldroidApplication
import de.hbch.traewelling.api.models.user.User

class BottomSearchViewModel(application: Application) : AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi

    suspend fun searchUsers(query: String): List<User> {
        return try {
            traewellingApi.userService.searchUsers(query).data
        } catch (_: Exception) {
            listOf()
        }
    }
}
