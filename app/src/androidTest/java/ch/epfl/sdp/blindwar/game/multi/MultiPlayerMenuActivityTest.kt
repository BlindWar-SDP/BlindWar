package ch.epfl.sdp.blindwar.game.multi

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MultiPlayerMenuActivityTest {
    @get:Rule
    var testRule = ActivityScenarioRule(
        MultiPlayerMenuActivity::class.java
    )

    @get:Rule
    var permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.RECORD_AUDIO)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun testCancelButton() {
        onView(withId(R.id.cancel_multi_menu)).perform(scrollTo(), ViewActions.click())
        intended(hasComponent(MainMenuActivity::class.java.name))
    }

    @Test
    fun testDisplayFriendButtonAndClose() {
        onView(withId(R.id.imageFriendsButton)).perform(scrollTo(), ViewActions.click())
        onView(withId(R.id.editTextLink)).check(matches(isDisplayed()))
    }

    @Test
    fun testCreateButton() {
        onView(withId(R.id.imageCreateButton)).perform(scrollTo(), ViewActions.click())
        intended(hasComponent(ChoseNumberOfPlayerActivity::class.java.name))
    }

    @Test
    fun testUseLinkFalse() {
        onView(withId(R.id.imageFriendsButton)).perform(scrollTo(), ViewActions.click())
        onView(withId(R.id.editTextLink)).perform(replaceText("htip"))
        clickOn(R.string.ok)
        onView(withId(R.id.imageFriendsButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testUseLinkTrue() {
        onView(withId(R.id.imageFriendsButton)).perform(scrollTo(), ViewActions.click())
        onView(withId(R.id.editTextLink)).perform(replaceText("https://blindwar.page.link/cker"))
        clickOn(R.string.ok)
        onView(withId(R.id.imageFriendsButton)).check(matches(isDisplayed()))
    }
}