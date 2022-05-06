package ch.epfl.sdp.blindwar


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import ch.epfl.sdp.blindwar.login.UserNewInfoActivity
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserNewInfoActivityTest : TestCase() {

    private val strNotDefault = "notDefaultText"
    private val validPseudo = "validPseudo"
    private val email = "test@bot.ch"
    private val password = "testtest"

    @get:Rule
    var testRule = ActivityScenarioRule(
        UserNewInfoActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
        closeSoftKeyboard()
        // ensure login
        runBlocking {
            await(Firebase.auth.signInWithEmailAndPassword(email, password))
        }
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun placeholder() {
        assert(true)
    }

    /**




    //    @Test

    //    fun testLayoutInvisibility() {
    //        putNewUserExtra(true)
    //        val invisibleIds = listOf(
    //            R.id.NU_Cancel_Btn,
    //            R.id.NU_deleteProfile
    //        )
    //        for (id in invisibleIds) {
    //            onView(withId(id))
    //                .check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    //        }
    //    }

    @Test
    fun testLayoutVisibility() {
    val visibleIds = listOf(
    R.id.NU_pseudo,
    R.id.NU_FirstName,
    R.id.NU_LastName,
    R.id.NU_additional_info,
    R.id.NU_Confirm_Btn,
    R.id.NU_Cancel_Btn,
    R.id.NU_deleteProfile
    )
    for (id in visibleIds) {
    onView(withId(id))
    .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
    }

     */
    @Test
    fun testNewUser_pseudo() {
        Firebase.auth.signOut()
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(scrollTo())
            .perform(click())
        assertDisplayed(R.string.new_user_wrong_pseudo_text)
        clickOn(android.R.string.ok)
    }

    @Test
    fun testNewUser_hideButton() {
        Firebase.auth.signOut()
        val invisibleID = listOf(
            R.id.NU_deleteProfile
        )
        for (id in invisibleID) {
            onView(withId(id))
                .perform(scrollTo())
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        }
    }


    @Test
    fun testPseudoTooShort() {
        onView(withId(R.id.NU_pseudo))
            .perform(replaceText("P"))
        onView(withId(R.id.NU_Confirm_Btn))
            .perform(scrollTo())
            .perform(click())
        assertDisplayed(R.string.new_user_wrong_pseudo_text)
        clickOn(android.R.string.ok)
    }


    @Test
    fun testChooseImage() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val oldPackageName = device.currentPackageName

        onView(withId(R.id.NU_editProfilePicture))
            .perform(scrollTo())
            .perform(click())

        // Press back until we get back to our activity
        var currentPackageName: String
        do {
            device.pressBack()
            currentPackageName = device.currentPackageName
        } while (currentPackageName != oldPackageName)

        onView(withId(R.id.NU_Confirm_Btn))
            .perform(scrollTo())
            .perform(click())
        intended(hasComponent(MainMenuActivity::class.java.name))
    }


    //    @Test
    //    fun testNewUser() {
    //        putNewUserExtra(true)
    //        onView(withId(R.id.NU_pseudo))
    //            .perform(replaceText(validPseudo))
    //        clickOn(R.id.NU_Confirm_Btn)
    //        intended(hasComponent(MainMenuActivity::class.java.name))
    //    }

    /**
    @Test
    fun testUpdateUser() {
    onView(withId(R.id.NU_pseudo))
    .perform(replaceText(validPseudo))
    clickOn(R.id.NU_Confirm_Btn)
    //intended(hasComponent(MainMenuActivity::class.java.name))
    }


    @Test
    fun testAdditionalInfo() {
    clickOn(R.id.NU_additional_info)
    intended(hasComponent(UserAdditionalInfoActivity::class.java.name))
    }

    fun testDeleteButton_cancel() {
    closeSoftKeyboard()
    clickOn(R.id.NU_deleteProfile)
    assertDisplayed(R.string.account_deletion_text)
    clickOn(android.R.string.cancel)
    }

    @Test
    fun testDeleteButton_ok_cancel() {
    closeSoftKeyboard()
    onView(withId(R.id.NU_deleteProfile))
    .perform(click())
    assertDisplayed(R.string.account_deletion_text)
    clickOn(android.R.string.ok)
    assertDisplayed(R.string.account_deletion_confirm_text)
    clickOn(android.R.string.cancel)
    }

    // This test delete Test account from database... we should use the emulator
    //    @Test
    //    fun testDeleteButton_ok_ok() {
    //        closeSoftKeyboard()
    //        onView(withId(R.id.NU_deleteProfile))
    //            .perform(click())
    //        assertDisplayed(R.string.account_deletion_text)
    //        clickOn(android.R.string.ok)
    //        assertDisplayed(R.string.account_deletion_confirm_text)
    //        clickOn(android.R.string.ok)
    //        intended(hasComponent(SplashScreenActivity::class.java.name))
    //    }

    @Test
    fun testCancelBtnOK() {
    closeSoftKeyboard()
    //        putNewUserExtra(false)
    clickOn(R.id.NU_Cancel_Btn)
    assertDisplayed(R.string.alert_dialogue_cancel_text)
    clickOn(android.R.string.ok)
    intended(hasComponent(MainMenuActivity::class.java.name))
    }

    @Test
    fun testCancelBtnCancel() {
    closeSoftKeyboard()
    //        putNewUserExtra(false)
    clickOn(R.id.NU_Cancel_Btn)
    assertDisplayed(R.string.alert_dialogue_cancel_text)
    clickOn(android.R.string.cancel)
    assertDisplayed(R.id.NU_Cancel_Btn)
    }

    @Test
    fun testBackButton() {

    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    assertTrue("Back button can't be pressed", device.pressBack())
    }
     **/


    /**
     *
     * Test of old kotlin activity additional info
     *

    @Before
    fun setup() {
    Intents.init()
    closeSoftKeyboard()
    testRule.scenario.onActivity {
    val bundle = Bundle()
    bundle.putBoolean("newUser", false)
    it.startActivity(it.intent.putExtras(bundle))
    }
    }

    @After
    fun cleanup() {
    Intents.release()
    }

    @Test
    fun testDescription() {
    typeTo(R.id.NUA_description, "My Description")
    closeSoftKeyboard()
    clickOn(R.id.NUA_Confirm_Btn)
    intended(hasComponent(UserNewInfoActivity::class.java.name))
    }

    @Test
    fun testDatePicker() {
    clickOn(R.id.NUA_select_birthdate)
    assertDisplayed(R.string.new_user_birthdatePicker)
    clickOn(android.R.string.ok)
    clickOn(R.id.NUA_Confirm_Btn)
    intended(hasComponent(UserNewInfoActivity::class.java.name))
    }

    @Test
    fun testSpinner() {
    clickSpinnerItem(R.id.gender_spinner, 3)
    clickOn(R.id.NUA_Confirm_Btn)
    intended(hasComponent(UserNewInfoActivity::class.java.name))
    }

    /**
    @Test
    fun testCancel() {
    clickOn(R.id.NUA_Cancel_Btn)
    intended(hasComponent(UserNewInfoActivity::class.java.name))
    }
    **/

    @Test
    fun testResetBirthdate() {
    closeSoftKeyboard()
    assertClickable(R.id.NUA_reset_birthdate)
    }


     */
}