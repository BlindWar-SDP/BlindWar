package ch.epfl.sdp.blindwar

import ch.epfl.sdp.blindwar.ui.AppStatistics
import ch.epfl.sdp.blindwar.ui.User
import android.net.Uri
import ch.epfl.sdp.blindwar.ui.UserBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
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

    private lateinit var testUser1: User

    private lateinit var testUser2: User

    @Before
    fun setUp() {
        testUser1 = UserBuilder().setBirthDate(birthDate1)
            .setEmail(email1)
            .setFirstName(firstName1)
            .setLastName(lastName1)
            .setImage(profilePicture)
            .setStats(userStatistics)
            .setPseudo(pseudo1)
            .build()

        testUser2 = UserBuilder().setBirthDate(birthDate2)
            .setEmail(email2)
            .setFirstName(firstName2)
            .setLastName(lastName2)
            .setImage(profilePicture)
            .setStats(userStatistics)
            .setPseudo(pseudo2)
            .build()
    }

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
        val testUser = testUser1.builder().setPseudo(pseudo2).build()
        assertEquals(testUser.pseudo, pseudo2)
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
        val testUser = testUser1.builder()
            .setStats(testUser1.userStatistics)
            .build()

        assertEquals(testUser.userStatistics, testUser1.userStatistics)
    }
}
