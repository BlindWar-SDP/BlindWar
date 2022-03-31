package ch.epfl.sdp.blindwar.database

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.ui.ProfileActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UserDatabaseTest : TestCase() {

    private val testUID = "JOJO"

    @get:Rule
    var testRule = ActivityScenarioRule(
        ProfileActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun setEloCorrectly() {
        UserDatabase.setElo(testUID, 1000)
        // should check elo wtih future
    }

    @Test
    fun addProfilePictureCorrectly() {
        UserDatabase.addProfilePicture(testUID, "")
        // should check path with futures
    }

    @Test
    fun getImageReferenceCorrectly() {
        TestCase.assertNotNull(UserDatabase.getImageReference(testUID))

    }

    @Test
    fun getEloReferenceCorrectly() {
        TestCase.assertNotNull(UserDatabase.getEloReference(testUID))
    }
}