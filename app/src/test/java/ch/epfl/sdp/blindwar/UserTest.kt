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
    private var birthdate1: Long = 1312341234
    private var birthdate2: Long = 1312311111
    private var userStatistics: AppStatistics = AppStatistics()
    private var gender1 = "Female"
    private var gender2 = "Male"
    private var description1 = "Male"
    private var description2 = "Male"
    private var uid1 = "Male"
    private var uid2 = "Male"
    private var profilePicture1: String = "image1"
    private var profilePicture2: String = "image2"

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
            .setBirthdate(birthdate1)
            .setProfilePicture(profilePicture1)
            .setUid(uid1)
            .setGender(gender1)
            .setDescription(description1)
            .build()

        testUser2 = User.Builder()
            .setEmail(email2)
            .setStats(userStatistics)
            .setPseudo(pseudo2)
            .setFirstName(firstName2)
            .setLastName(lastName2)
            .setBirthdate(birthdate2)
            .setProfilePicture(profilePicture2)
            .setUid(uid2)
            .setGender(gender2)
            .setDescription(description2)
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
    fun getBirthdate() {
        assertEquals(testUser1.birthDate, birthdate1)
    }

    @Test
    fun getPseudo() {
        assertEquals(testUser1.pseudo, pseudo1)
    }
    @Test
    fun getUid() {
        assertEquals(testUser1.uid, uid1)
    }
    @Test
    fun getDescription() {
        assertEquals(testUser1.description, description1)
    }
    @Test
    fun getGender() {
        assertEquals(testUser1.gender, gender1)
    }
    @Test
    fun getProfilePicture() {
        assertEquals(testUser1.profilePicture, profilePicture1)
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
        val testUser = User.Builder().setBirthdate(birthdate1).build()
        assertEquals(testUser.birthDate, birthdate1)
    }

    @Test
    fun setProfilePicture() {
        val testUser = User.Builder().setProfilePicture(profilePicture1).build()
        assertEquals(testUser.profilePicture, profilePicture1)
    }
    @Test
    fun setGender() {
        val testUser = User.Builder().setGender(gender1).build()
        assertEquals(testUser.gender, gender1)
    }
    @Test
    fun setBirthdate() {
        val testUser = User.Builder().setBirthdate(birthdate1).build()
        assertEquals(testUser.birthDate, birthdate1)
    }
    @Test
    fun setDescription() {
        val testUser = User.Builder().setDescription(description1).build()
        assertEquals(testUser.description, description1)
    }
    @Test
    fun setUid() {
        val testUser = User.Builder().setUid(uid1).build()
        assertEquals(testUser.uid, uid1)
    }


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
