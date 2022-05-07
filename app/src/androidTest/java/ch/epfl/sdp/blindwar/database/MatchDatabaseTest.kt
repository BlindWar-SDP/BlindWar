package ch.epfl.sdp.blindwar.database

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.profile.fragments.ProfileFragment
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MatchDatabaseTest : TestCase() {

    //private val dbReference =

    @Before
    fun init() {

    }

    @Test
    fun testMatchCreation() {
        launchFragmentInContainer<ProfileFragment>()
        //
        //MatchDatabase.createMatch(User(), 1, GameUtil.gameInstance, db)
    }
}