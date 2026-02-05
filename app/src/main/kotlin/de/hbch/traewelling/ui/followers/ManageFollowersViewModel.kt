package de.hbch.traewelling.ui.followers

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import de.hbch.traewelling.TraewelldroidApplication
import de.hbch.traewelling.api.models.user.User

class ManageFollowersViewModel(application: Application) : AndroidViewModel(application) {
    private val traewellingApi = (application as TraewelldroidApplication).traewellingApi

    suspend fun getFollowers(page: Int = 0): List<User> {
        val users = try {
            val response = traewellingApi.userService.getFollowers(page)
            response.body()?.data ?: listOf()
        } catch (_: Exception) {
            listOf()
        }

        return users
    }

    suspend fun removeFollower(userId: Int): Boolean {
        return try {
            val response = traewellingApi.userService.removeFollower(userId)
            response.isSuccessful
        } catch (ex: Exception) {
            Log.e("Error", ex.message ?: "")
            false
        }
    }

    suspend fun getFollowings(page: Int = 0): List<User> {
        val users = try {
            val response = traewellingApi.userService.getFollowings(page)
            response.body()?.data ?: listOf()
        } catch (_: Exception) {
            listOf()
        }

        return users
    }

    suspend fun unfollowUser(userId: Int): Boolean {
        return try {
            val response = traewellingApi.userService.removeFollowing(userId)
            response.isSuccessful
        } catch (ex: Exception) {
            Log.e("Error", ex.message ?: "")
            false
        }
    }

    suspend fun getFollowRequests(page: Int = 0): List<User> {
        return try {
            val response = traewellingApi.userService.getFollowRequests(page)
            response.body()?.data ?: listOf()
        } catch (_: Exception) {
            listOf()
        }
    }

    suspend fun acceptFollowRequest(userId: Int): Boolean {
        return try {
            val response = traewellingApi.userService.acceptFollowRequest(userId)
            response.isSuccessful
        } catch (ex: Exception) {
            Log.e("Error", ex.message ?: "")
            false
        }
    }

    suspend fun declineFollowRequest(userId: Int): Boolean {
        return try {
            val response = traewellingApi.userService.declineFollowRequest(userId)
            response.isSuccessful
        } catch (ex: Exception) {
            Log.e("Error", ex.message ?: "")
            false
        }
    }
}
