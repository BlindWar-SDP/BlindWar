package ch.epfl.sdp.blindwar.profile.fragments

/* TODO: Debug tests
@RunWith(AndroidJUnit4::class)
class StatisticsActivityTest : TestCase() {

    private val placeholder = 1

    @get:Rule
    var testRule = ActivityScenarioRule(
        StatisticsActivity::class.java
    )

    @Before
    fun setup() {
        Intents.init()
        closeSoftKeyboard()
    }

    @After
    fun cleanup() {
        Intents.release()
    }

    @Test
    fun placeholderTest() {
        assertEquals(placeholder, 1)
    }

    /**
     * Logs in
     * Goes to solo mode.
     * Pass every(fail everything quickly)
     * and go statistics page again.
     * Temporarily, just open statistics while logged in
     */
    @Test
    fun statisticsUpdatedCorrectly() {
        val testEmail = "test@bot.ch"
        val testPassword = "testtest"
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(testEmail, testPassword)
        try {
            Tasks.await<AuthResult>(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        Thread.sleep(1000)
        launchFragmentInContainer<StatisticsFragment>()
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.logoutButton)).perform(ViewActions.click())
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.logoutButton)).perform(ViewActions.click())
    }
}*/