package ch.epfl.sdp.blindwar.game

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import ch.epfl.sdp.blindwar.data.music.MusicMetadata
import ch.epfl.sdp.blindwar.game.viewmodels.GameViewModel
import ch.epfl.sdp.blindwar.game.util.Tutorial
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import java.util.concurrent.ExecutionException

@RunWith(AndroidJUnit4::class)
class GameTutorialTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testNextRound() {
        val gameTutorial = GameViewModel(Tutorial.gameInstance, context, context.resources)
        gameTutorial.init()
        val round = Tutorial.ROUND

        // Iterate 10 times since we have 10 different musics in tutorial
        val toPlay: MutableSet<MusicMetadata> = Tutorial.gameInstance.onlinePlaylist.songs.toMutableSet()
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
            Tasks.await<AuthResult>(login)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val gameTutorial = GameViewModel(Tutorial.gameInstance, context, context.resources)
        gameTutorial.init()
        val round = Tutorial.ROUND
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
        val gameTutorial = GameViewModel(Tutorial.gameInstance, context, context.resources)
        gameTutorial.init()
        goodGuess(gameTutorial)
        goodGuess(gameTutorial)

        assertThat(gameTutorial.score, `is`(2))
    }

    @Test
    fun testUpperCaseGuess() {
        val gameTutorial = GameViewModel(Tutorial.gameInstance, context, context.resources)
        gameTutorial.init()
        gameTutorial.nextRound()
        val music1 = gameTutorial.currentMetadata()
        music1?.let { gameTutorial.guess(it.title.uppercase(Locale.getDefault()), false) }

        assertThat(gameTutorial.score, `is`(1))
    }

    @Test
    fun testOneGoodAndOneBadGuesses() {
        val gameTutorial = GameViewModel(Tutorial.gameInstance, context, context.resources)
        gameTutorial.init()
        goodGuess(gameTutorial)
        badGuess(gameTutorial)

        assertThat(gameTutorial.score, `is`(1))
    }

    private fun goodGuess(gameViewModelTutorial: GameViewModel) {
        gameViewModelTutorial.nextRound()
        gameViewModelTutorial.guess(gameViewModelTutorial.currentMetadata()?.title!!, false)
    }

    private fun badGuess(gameViewModelTutorial: GameViewModel) {
        gameViewModelTutorial.nextRound()
        gameViewModelTutorial.guess("THIS IS NOT A CORRECT TITLE", false)
    }
}