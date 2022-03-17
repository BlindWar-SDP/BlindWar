package ch.epfl.sdp.blindwar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserTest {

    private val firstName = "Test"
    private val lastName = "Dude"
    private val email = "testdude@epfl.ch"
    private val screenName = "Screen Name"
    private val screenName2 = "Screen Name2"

    private var testUser = User(firstName, lastName, email, screenName, AppStatistics())

    @Test
    fun getFirstName() {
        assertEquals(testUser.firstName, firstName)
    }

    @Test
    fun getLastName() {
        assertEquals(testUser.lastName, lastName)
    }

    @Test
    fun getEmail() {
        assertEquals(testUser.email, email)
    }

    @Test
    fun getScreenName() {
        assertEquals(testUser.screenName, screenName)
    }

    @Test
    fun setScreenName() {
        testUser.screenName = screenName2
        assertEquals(testUser.screenName, screenName2)
    }
    /*
    @Test
    fun getProfilePicture() {
        assertNull(testUser.profilePicture)
    }

    @Test
    fun setProfilePicture() {
        assertNull(testUser.profilePicture)
    }*/

    @Test
    fun getUserStatistics() {
        assertEquals(testUser.userStatistics.elo, 1000)
    }

    @Test
    fun setUserStatistics() {
        testUser.userStatistics.eloUpdateWin(1005)
        assertEquals(testUser.userStatistics.elo, 1015)
    }
}