package ch.epfl.sdp.blindwar.game.viewmodel

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import ch.epfl.sdp.blindwar.data.music.metadata.MusicMetadata
import ch.epfl.sdp.blindwar.game.util.Tutorial
import ch.epfl.sdp.blindwar.game.viewmodels.GameViewModelMulti
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.ExecutionException

// TODO : Adapt test for multiplayer game
@RunWith(AndroidJUnit4::class)
class GameViewModelMultiTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testNextRound() {
        val gameViewModelMulti = GameViewModelMulti(Tutorial.gameInstance, context, context.resources)
        gameViewModelMulti.init()
        val round = Tutorial.ROUND

        // Iterate 10 times since we have 10 different musics in tutorial
        val toPlay: MutableSet<MusicMetadata> = Tutorial.gameInstance.onlinePlaylist.songs.toMutableSet()
        for (i in 0 until round) {
            gameViewModelMulti.nextRound()
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

        val gameViewModelMulti = GameViewModelMulti(Tutorial.gameInstance, context, context.resources)
        gameViewModelMulti.init()
        val round = Tutorial.ROUND
        for (i in 0 until round) {
            goodGuess(gameViewModelMulti)
            val round = gameViewModelMulti.round
            val fails = gameViewModelMulti.round - gameViewModelMulti.score
            assertThat(fails, `is`(0))
        }
        FirebaseAuth.getInstance().signOut()
        val logout: Unit = FirebaseAuth.getInstance().signOut()
        Thread.sleep(1000)
    }


    @Test
    fun testTwoGoodGuesses() {
        val gameViewModelMulti = GameViewModelMulti(Tutorial.gameInstance, context, context.resources)
        gameViewModelMulti.init()
        goodGuess(gameViewModelMulti)
        goodGuess(gameViewModelMulti)

        assertThat(gameViewModelMulti.score, `is`(2))
    }

    @Test
    fun testUpperCaseGuess() {
        val gameViewModelMulti = GameViewModelMulti(Tutorial.gameInstance, context, context.resources)
        gameViewModelMulti.init()
        gameViewModelMulti.nextRound()
        val music1 = gameViewModelMulti.currentMetadata()
        music1?.let { gameViewModelMulti.guess(it.title.uppercase(Locale.getDefault()), false) }

        assertThat(gameViewModelMulti.score, `is`(1))
    }

    @Test
    fun testOneGoodAndOneBadGuesses() {
        val gameViewModelMulti = GameViewModelMulti(Tutorial.gameInstance, context, context.resources)
        gameViewModelMulti.init()
        goodGuess(gameViewModelMulti)
        badGuess(gameViewModelMulti)

        assertThat(gameViewModelMulti.score, `is`(1))
    }

    private fun goodGuess(gameViewModelMulti: GameViewModelMulti) {
        gameViewModelMulti.nextRound()
        gameViewModelMulti.guess(gameViewModelMulti.currentMetadata()?.title!!, false)
    }

    private fun badGuess(gameViewModelMulti: GameViewModelMulti) {
        gameViewModelMulti.nextRound()
        gameViewModelMulti.guess("THIS IS NOT A CORRECT TITLE", false)
    }
}