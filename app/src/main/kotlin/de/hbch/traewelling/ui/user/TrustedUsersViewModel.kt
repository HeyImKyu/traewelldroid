package de.hbch.traewelling.ui.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import de.hbch.traewelling.TraewelldroidApplication
import de.hbch.traewelling.api.models.user.CreateTrustedUser
import de.hbch.traewelling.api.models.user.TrustedUser
import java.time.ZonedDateTime

class TrustedUsersViewModel(application: Application): AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi

    suspend fun getTrustedUsers(): List<TrustedUser>? {
        return try {
            val response = traewellingApi.userService.getTrustedUsers()
            response.body()?.data
        } catch (_: Exception) {
            null
        }
    }

    suspend fun addTrustedUser(userId: Int, expiresAt: ZonedDateTime?): Boolean {
        return try {
            val response = traewellingApi.userService.trustUser(CreateTrustedUser(userId, expiresAt))
            response.isSuccessful
        } catch (_: Exception) {
            false
        }
    }

    suspend fun removeTrustedUser(userId: Int): Boolean {
        return try {
            val response = traewellingApi.userService.removeTrustedUser(userId)
            response.isSuccessful
        } catch (_: Exception) {
            false
        }
    }

    suspend fun getTrustingUsers(): List<TrustedUser>? {
        return try {
            val response = traewellingApi.userService.getTrustingUsers()
            response.body()?.data
        } catch (_: Exception) {
            null
        }
    }
}
