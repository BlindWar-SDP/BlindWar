package ch.epfl.sdp.blindwar.game.solo

/**
@RunWith(AndroidJUnit4::class)
class SoloMenuActivityTest : TestCase() {

    @get:Rule
    var testRule = ActivityScenarioRule(
        SoloMenuActivity::class.java
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
    fun testOnlineMusicButton() {
        onView(withId(R.id.onlineMusicButton)).check(matches(isClickable()))
    }

    @Test
    fun testLocalMusicButton() {
        onView(withId(R.id.localMusicButton)).check(matches(isClickable()))
    }

    @Test
    fun testTutorialMusicButton() {
        onView(withId(R.id.tutorialMusicButton)).check(matches(isClickable()))
    }


}**/