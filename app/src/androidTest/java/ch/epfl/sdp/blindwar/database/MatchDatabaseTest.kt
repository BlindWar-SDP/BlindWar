package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MatchDatabaseTest : TestCase() {

    //private val dbReference =

    @Before
    fun init() {

    }

    @Test
    fun testMatchCreation() {
        launchFragmentInContainer<ProfileFragment>()
        val match = MatchDatabase.createMatch(
            "test",
            "test",
            1000,
            2,
            GameInstance.Builder().build(),
            Firebase.firestore,
            false
        )
        assertTrue(match?.listPlayers!!.contains("test"))
    }

    @Test
    fun testMatchCreationNull() {
        launchFragmentInContainer<ProfileFragment>()
        val match = MatchDatabase.createMatch(
            "",
            "test",
            1000,
            2,
            GameInstance.Builder().build(),
            Firebase.firestore,
            false
        )
        assertTrue(match == null)
    }

    @Test
    fun testMatchRemove() {
        launchFragmentInContainer<ProfileFragment>()
        val match = MatchDatabase.createMatch(
            "test",
            "test",
            1000,
            2,
            GameInstance.Builder().build(),
            Firebase.firestore,
            false
        )
        MatchDatabase.removeMatch("test", FirebaseFirestore.getInstance())
    }

    @Test
    fun testGetMatch() {
        launchFragmentInContainer<ProfileFragment>()
        val match = MatchDatabase.createMatch(
            "test",
            "test",
            1000,
            2,
            GameInstance.Builder().build(),
            Firebase.firestore,
            false
        )
        assertTrue(MatchDatabase.getMatchSnapshot("test", Firebase.firestore) != null)
    }
}