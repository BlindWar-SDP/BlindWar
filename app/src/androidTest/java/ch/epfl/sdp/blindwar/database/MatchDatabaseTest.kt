package ch.epfl.sdp.blindwar.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.game.model.config.GameInstance
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MatchDatabaseTest : TestCase() {

    @Before
    fun init() {

    }

    @Test
    fun testMatchCreationNull() {
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
    fun testConnectFail() {
        val match = Match("", 0, mutableListOf("t"), maxPlayer = 1)
        val result = MatchDatabase.connect(match, User(), Firebase.firestore)
        assertTrue(result == null)
    }
}