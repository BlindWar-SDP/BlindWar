package ch.epfl.sdp.blindwar.profile.fragments

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DisplayHistoryFragmentTest {

    @Test
    fun testNewInstance() {
        val d = DisplayHistoryFragment.newInstance("yo")
        assertTrue(d.arguments?.getString(DisplayHistoryFragment.HISTORY_TYPE) == "yo")
    }
}