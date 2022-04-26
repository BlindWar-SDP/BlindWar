package ch.epfl.sdp.blindwar.user

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson


private const val OFFLINE = "offline"
private const val USER = "user"

interface UserCache {

    fun createCache(context: Context) {
        // is there already an offline data?
        if (cacheAsString(context).isEmpty()) {
            // if not, create a new one with empty user
            writeCache(context)
        }
    }

    fun readCache(context: Context): User {
        var user = User()
        val offlineStr = cacheAsString(context)
        if (offlineStr.isNotEmpty()) {
            user = Gson().fromJson(offlineStr, User::class.java)
        }
        return user
    }

    fun writeCache(context: Context, user: User = User()) {
        context.getSharedPreferences(OFFLINE, AppCompatActivity.MODE_PRIVATE)
            .edit()
            .putString(USER, Gson().toJson(user))
            .apply()
    }

    fun removeCache(context: Context) {
        context.getSharedPreferences(OFFLINE, AppCompatActivity.MODE_PRIVATE)
            .edit()
            .remove(USER)
            .apply()
    }

    fun setOffline(context: Context, bool: Boolean) {
        context.getSharedPreferences(OFFLINE, AppCompatActivity.MODE_PRIVATE)
            .edit()
            .putBoolean(OFFLINE, bool)
            .apply()
    }

    fun isOffline(context: Context): Boolean {
        return context.getSharedPreferences(OFFLINE, AppCompatActivity.MODE_PRIVATE)
            .getBoolean(OFFLINE, false)
    }

    fun updateServerFromCache(context: Context) {
        val user = readCache(context)
        UserDatabase.updateUser(user)
    }

    fun updateServerFromCacheFirstLogin(context: Context, user: FirebaseUser) {
        // update user info if there is a local data (1st login in when offline)
        val userCache = readCache(context)
        userCache.uid = user.uid
        user.email?.let {
            userCache.email = it
        }
        UserDatabase.updateUser(userCache)
    }

    private fun cacheAsString(context: Context): String {
        val offline = context.getSharedPreferences(OFFLINE, AppCompatActivity.MODE_PRIVATE)
            .getString(USER, null)
        offline?.let {
            return it
        } ?: return ""
    }
}