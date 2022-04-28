package ch.epfl.sdp.blindwar.menu.multi

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerActivity
import ch.epfl.sdp.blindwar.game.multi.MultiPlayerFriendActivity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MultiPlayerActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        MultiPlayerActivity::class.java
    )

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

    @Test
    fun friendButton() {
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.imageFriendsButton))
            .perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(MultiPlayerFriendActivity::class.java.name))
    }

    /*@Test
    fun randomButton() {
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.imageRandomButton))
            .perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(MultiPlayerRandomActivity::class.java.name))
    }*/
}