package ch.epfl.sdp.blindwar


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.login.UserNewInfoActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserNewInfoActivitySignedOutTest : TestCase() {

    private val strNotDefault = "notDefaultText"
    private val email = "test@test.test"
    private val password = "testTest"
    private val validPseudo = "validPseudo"

    @get:Rule
    var testRule = ActivityScenarioRule(
        UserNewInfoActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
        closeSoftKeyboard()
        // ensure login
        Firebase.auth.signOut()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun placeholder() {
        assert(true)
    }

    @Test
    fun testNewUser_hideButton() {
        val invisibleID = listOf(
            R.id.NU_Cancel_Btn,
            R.id.NU_deleteProfile
        )
        for (id in invisibleID) {
            onView(withId(id))
                .perform(scrollTo())
                .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        }
    }
}