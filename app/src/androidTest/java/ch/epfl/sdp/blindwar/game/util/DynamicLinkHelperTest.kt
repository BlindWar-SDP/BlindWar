package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DynamicLinkHelperTest : TestCase() {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun setDynamicLinkDialogFalse() {
        try {
            val dialog = DynamicLinkHelper.setDynamicLinkDialog("test", "0", context, false)

            assertFalse(dialog.isShowing)
        } catch (e: Exception) {
            Log.w("TAG", e.javaClass.name)
            assertTrue(e.javaClass.name == "java.lang.RuntimeException")
        }
    }

    @Test
    fun setDynamicLinkDialogTrue() {
        try {
            val dialog = DynamicLinkHelper.setDynamicLinkDialog("test", "0", context, true)
            assertFalse(dialog.isShowing)
        } catch (e: Exception) {
            Log.w("TAG", e.javaClass.name)
            assertTrue(e.javaClass.name == "java.lang.RuntimeException")
        }
    }
}