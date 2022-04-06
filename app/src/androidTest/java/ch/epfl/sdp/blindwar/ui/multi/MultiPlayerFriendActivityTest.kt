package ch.epfl.sdp.blindwar.ui.multi

import androidx.test.espresso.intent.Intents
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test

class MultiPlayerFriendActivityTest {

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun onCreate() {
        //Placeholder
        assertEquals(1,1)
    }
}