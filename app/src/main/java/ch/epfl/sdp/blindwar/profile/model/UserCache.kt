package ch.epfl.sdp.blindwar.user

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson


private const val BLIND_WAR = "blind_war"
private const val USER = "user"
private const val OFFLINE = "offline"

interface UserCache {

    /**
     * create local user if not already exist
     *
     * @param context
     */
    fun createCache(context: Context) {
        // create if not already exist
        if (cacheAsString(context).isEmpty()) {
            writeCache(context)
        }
    }

    /**
     * read local data to user
     *
     * @param context
     * @return User that have local data
     */
    fun readCache(context: Context): User {
        var user = User()
        val localStr = cacheAsString(context)
        if (localStr.isNotEmpty()) {
            user = Gson().fromJson(localStr, User::class.java)
        }
        return user
    }

    /**
     * write local data from user
     *
     * @param context
     * @param user
     */
    fun writeCache(context: Context, user: User = User()) {
        context.getSharedPreferences(BLIND_WAR, AppCompatActivity.MODE_PRIVATE)
            .edit()
            .putString(USER, Gson().toJson(user))
            .apply()
    }

    /**
     * delete local data
     *
     * @param context
     */
    fun removeCache(context: Context) {
        context.getSharedPreferences(BLIND_WAR, AppCompatActivity.MODE_PRIVATE)
            .edit()
            .remove(USER)
            .apply()
    }

    /**
     * set the local data about network connection with a boolean
     *
     * @param context
     * @param bool
     */
    fun setOffline(context: Context, bool: Boolean) {
        context.getSharedPreferences(BLIND_WAR, AppCompatActivity.MODE_PRIVATE)
            .edit()
            .putBoolean(OFFLINE, bool)
            .apply()
    }

    /**
     * get the local data about network connection
     *
     * @param context
     * @return
     */
    fun isOffline(context: Context): Boolean {
        return context.getSharedPreferences(BLIND_WAR, AppCompatActivity.MODE_PRIVATE)
            .getBoolean(OFFLINE, false)
    }

    /**
     * push local data to firebase server
     *
     * @param context
     */
    fun updateServerFromCache(context: Context) {
        val user = readCache(context)
        UserDatabase.updateUser(user) // TODO this should be addUser instead ? (no stats update, but needed as no offline was... but now there is so yes !!)
    }

    /**
     * create user from local data at first online connection
     *
     * if user first connect offline, no id neither email are available
     * thus, we get it from the FirebaseUser
     *
     * @param context
     * @param user
     */
    fun updateServerFromCacheFirstLogin(context: Context, user: FirebaseUser) {
        // update user info if there is a local data (1st login in when offline)
        val userCache = readCache(context)
        userCache.uid = user.uid
        user.email?.let {
            // there exist connection method not requiring email
            userCache.email = it
        }
        UserDatabase.updateUser(userCache) // TODO this should be addUser instead ?
    }

    /**
     * read local storage and return it as a String,
     * which is empty if no local data
     *
     * @param context
     * @return
     */
    private fun cacheAsString(context: Context): String {
        val offline = context.getSharedPreferences(BLIND_WAR, AppCompatActivity.MODE_PRIVATE)
            .getString(USER, null)
        offline?.let {
            return it
        } ?: return ""
    }
}