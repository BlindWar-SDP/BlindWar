package ch.epfl.sdp.blindwar

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest : TestCase() {
    @get:Rule
    var testRule = ActivityScenarioRule(
        LoginActivity::class.java
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
    fun testLoginButton() {
        onView(withId(R.id.button_SignIn))
            .perform(click())

        var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        if (user == null) {
            // WHAT TO DO HERE ??
        } else {
            intended(hasComponent(MainMenuActivity::class.java.name))
        }
    }
}