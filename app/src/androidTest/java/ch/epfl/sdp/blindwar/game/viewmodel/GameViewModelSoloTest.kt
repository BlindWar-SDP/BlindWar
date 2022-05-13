package ch.epfl.sdp.blindwar.game.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.util.GameUtil
import ch.epfl.sdp.blindwar.game.viewmodels.GameViewModelSolo
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.ExecutionException

@RunWith(AndroidJUnit4::class)
class GameViewModelSoloTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testNextRound() {
        val gameTutorial = GameViewModelSolo(GameUtil.gameInstanceSolo, context, context.resources)
        gameTutorial.init()
        val round = GameUtil.ROUND

        // Iterate 10 times since we have 10 different musics in tutorial
        val toPlay: MutableSet<MusicMetadata> =
            GameUtil.gameInstanceSolo.onlinePlaylist?.songs!!.toMutableSet()
        for (i in 0 until round) {
            gameTutorial.nextRound()
            //assertThat(toPlay.contains(gameTutorial.currentMetadata()), `is`(true))
        }
    }

    @Test
    fun gameWithLogin() {
        val testEmail = "test@bot.ch"
        val testPassword = "testtest"
        val login: Task<AuthResult> = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(testEmail, testPassword)
        try {
            Tasks.await(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val gameTutorial = GameViewModelSolo(GameUtil.gameInstanceSolo, context, context.resources)
        gameTutorial.init()
        val round = GameUtil.ROUND
        for (i in 0 until round) {
            goodGuess(gameTutorial)
            val round = gameTutorial.round
            val fails = gameTutorial.round - gameTutorial.score
            assertThat(fails, `is`(0))
        }
        FirebaseAuth.getInstance().signOut()
        val logout: Unit = FirebaseAuth.getInstance().signOut()
        Thread.sleep(1000)
    }


    @Test
    fun testTwoGoodGuesses() {
        val gameTutorial = GameViewModelSolo(GameUtil.gameInstanceSolo, context, context.resources)
        gameTutorial.init()
        goodGuess(gameTutorial)
        goodGuess(gameTutorial)

        assertThat(gameTutorial.score, `is`(2))
    }

    @Test
    fun testUpperCaseGuess() {
        val gameTutorial = GameViewModelSolo(GameUtil.gameInstanceSolo, context, context.resources)
        gameTutorial.init()
        gameTutorial.nextRound()
        val music1 = gameTutorial.currentMetadata()
        music1?.let { gameTutorial.guess(it.title.uppercase(Locale.getDefault()), false) }

        assertThat(gameTutorial.score, `is`(1))
    }

    @Test
    fun testOneGoodAndOneBadGuesses() {
        val gameTutorial = GameViewModelSolo(GameUtil.gameInstanceSolo, context, context.resources)
        gameTutorial.init()
        goodGuess(gameTutorial)
        badGuess(gameTutorial)

        assertThat(gameTutorial.score, `is`(1))
    }

    private fun goodGuess(gameViewModelSoloTutorial: GameViewModelSolo) {
        gameViewModelSoloTutorial.nextRound()
        gameViewModelSoloTutorial.guess(gameViewModelSoloTutorial.currentMetadata()?.title!!, false)
    }

    private fun badGuess(gameViewModelSoloTutorial: GameViewModelSolo) {
        gameViewModelSoloTutorial.nextRound()
        gameViewModelSoloTutorial.guess("THIS IS NOT A CORRECT TITLE", false)
    }
}