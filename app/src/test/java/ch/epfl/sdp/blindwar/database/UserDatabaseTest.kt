package ch.epfl.sdp.blindwar.database

import android.media.Image
import ch.epfl.sdp.blindwar.AppStatistics
import ch.epfl.sdp.blindwar.User
import junit.framework.TestCase

class UserDatabaseTest : TestCase() {

    fun testGetRootNode() {}

    fun testAddUser() {
        val database = UserDatabase()
        val appStatistics = AppStatistics()
        val user = User("Jojo", "Joestar", "@", "JOJO")
        database.addUser(user)
    }
}