package ch.epfl.sdp.blindwar

import android.net.Uri
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserTest {

    private var email1 = "test1@epfl.ch"
    private var email2 = "test2@epfl.ch"
    private var firstName1 = "firstName1"
    private var lastName1 = "lastName1"
    private var firstName2 = "firstName2"
    private var lastName2 = "lastName2"
    private var pseudo1 = "Screen Name1"
    private var pseudo2 = "Screen Name2"
    private var birthDate1: Long= 1312341234
    private var birthDate2: Long= 1312341234
    private var userStatistics: AppStatistics = AppStatistics()
    private var profilePicture: Uri? = null


    private var testUser1 = User(email1, userStatistics, pseudo1, firstName1, lastName1, birthDate1, profilePicture)
    private var testUser2 = User(email2, userStatistics, pseudo2, firstName2, lastName2, birthDate2, profilePicture)

    @Test
    fun getFirstName() {
        assertEquals(testUser1.firstName, firstName1)
    }

    @Test
    fun getLastName() {
        assertEquals(testUser1.lastName, lastName1)
    }

    @Test
    fun getEmail() {
        assertEquals(testUser1.email, email1)
    }

    @Test
    fun getScreenName() {
        assertEquals(testUser1.pseudo, pseudo1)
    }

    @Test
    fun setScreenName() {
        testUser1.pseudo = pseudo2
        assertEquals(testUser1.pseudo, pseudo2)
    }
    /*
    @Test
    fun getProfilePicture() {
        assertNull(testUser1.profilePicture)
    }

    @Test
    fun setProfilePicture() {
        assertNull(testUser1.profilePicture)
        assertNull(testUser.profilePicture)
    }*/

    @Test
    fun getUserStatistics() {
        assertEquals(testUser1.userStatistics.elo, 1000)
    }

    @Test
    fun setUserStatistics() {
        testUser1.userStatistics.eloUpdateWin(1005)
        testUser1.userStatistics = testUser1.userStatistics
        assertEquals(testUser1.userStatistics.elo, 1015)
    }
}
