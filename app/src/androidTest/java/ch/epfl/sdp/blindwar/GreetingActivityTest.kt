package ch.epfl.sdp.blindwar

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingActivityTest {
    @Test
    fun checkMessage() {
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            GreetingActivity::class.java
        ).apply {
            putExtra(EXTRA_MESSAGE, "BlindWar")
        }
        ActivityScenario.launch<GreetingActivity>(intent)
            .use { onView(withId(R.id.textViewGreeting)).check(matches(withText("BlindWar"))) }
    }
}