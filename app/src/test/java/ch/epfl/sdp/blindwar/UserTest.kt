package ch.epfl.sdp.blindwar

import ch.epfl.sdp.blindwar.profile.model.AppStatistics
import ch.epfl.sdp.blindwar.profile.model.User
import org.junit.Assert.assertEquals
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
    private var birthDate1: Long = 1312341234
    private var birthDate2: Long = 1312311111
    private var userStatistics: AppStatistics = AppStatistics()
//    private var profilePicture: Uri? = null

    private lateinit var testUser1: User
    private lateinit var testUser2: User

    @Before
    fun setUp() {
        testUser1 = User.Builder()
            .setEmail(email1)
            .setStats(userStatistics)
            .setPseudo(pseudo1)
            .setFirstName(firstName1)
            .setLastName(lastName1)
            .setBirthDate(birthDate1)
//            .setImage(profilePicture)
            .build()

        testUser2 = User.Builder()
            .setEmail(email2)
            .setStats(userStatistics)
            .setPseudo(pseudo2)
            .setFirstName(firstName2)
            .setLastName(lastName2)
            .setBirthDate(birthDate2)
//            .setImage(profilePicture)
            .build()
    }

    // =========
    /* GETTER */
    // =========
    @Test
    fun getEmail() {
        assertEquals(testUser1.email, email1)
    }

    @Test
    fun getUserStatistics() {
        assertEquals(testUser1.userStatistics.elo, 1000)
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
    fun getBirthDate() {
        assertEquals(testUser1.birthDate, birthDate1)
    }

    @Test
    fun getPseudo() {
        assertEquals(testUser1.pseudo, pseudo1)
    }

    // =========
    /* SETTER */
    // =========
    @Test
    fun setEmail() {
        val testUser = User.Builder().setEmail(email1).build()
        assertEquals(testUser.email, email1)
    }

    @Test
    fun setStats() {
        val testUser = User.Builder().setStats(userStatistics).build()
        assertEquals(testUser.userStatistics, userStatistics)
    }

    @Test
    fun setPseudo() {
        val testUser = User.Builder().setPseudo(pseudo1).build()
        assertEquals(testUser.pseudo, pseudo1)
    }

    @Test
    fun setFirstName() {
        val testUser = User.Builder().setFirstName(firstName1).build()
        assertEquals(testUser.firstName, firstName1)
    }

    @Test
    fun setLastName() {
        val testUser = User.Builder().setLastName(lastName1).build()
        assertEquals(testUser.lastName, lastName1)
    }

    @Test
    fun setBirthDate() {
        val testUser = User.Builder().setBirthDate(birthDate1).build()
        assertEquals(testUser.birthDate, birthDate1)
    }

    /*
    @Test
    fun setImage() {
        val testUser = User.Builder().setImage(profilePicture).build()
        assertEquals(testUser.profilePicture, profilePicture)
    }
     */


    // =============
    /* MORE TESTS */
    // =============
    @Test
    fun updateStats() {
        testUser1.userStatistics.eloSetter(1005)
        val testUser = User.Builder()
            .setStats(testUser1.userStatistics)
            .build()
        assertEquals(testUser.userStatistics, testUser1.userStatistics)
    }

    @Test
    fun fromUser() {
        val testUser = User.Builder().fromUser(testUser1).build()
        assertEquals(testUser.email, email1)
        assertEquals(testUser.userStatistics, userStatistics)
        assertEquals(testUser.pseudo, pseudo1)
        assertEquals(testUser.firstName, firstName1)
        assertEquals(testUser.lastName, lastName1)
//        assertEquals(testUser.profilePicture, profilePicture)
    }

    @Test
    fun testToString() {
        assertEquals(testUser1.toString(), testUser1.pseudo)
    }
}
