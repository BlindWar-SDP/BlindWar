package ch.epfl.sdp.blindwar.ui.solo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.domain.game.*


class SoloMenuActivity : AppCompatActivity() {

    val localMusicModeHandler =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                // Handle the Intent
                val uri = intent?.data

                if (uri != null) {
                    val pickedDir = DocumentFile.fromTreeUri(this, uri)
                    val builder = StringBuilder()
                    for (file in pickedDir!!.listFiles()) {
                        builder.append(
                            """${file.name} -> ${file.length()}"""
                        )
                    }


                    val playlist: List<SongMetaData> = SongImageUrlConstants.META_DATA_TUTORIL_MUSICS_PER_AUTHOR.values.toList()

                    val gameParameter = GameParameter(3, funny = false, hint = true, 30000)

                    val gameConfig = GameConfig(
                        GameDifficulty.EASY,
                        GameFormat.SOLO, GameMode.REGULAR, gameParameter
                    )

                    val gameInstance = GameInstance(gameConfig, playlist)

                    val game = GameSoloFromLocalStorage(gameInstance, assets, pickedDir, applicationContext.contentResolver).also { it.init() }
                    game.play()
                    game.nextRound(true)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_menu)
    }

    fun yourMusicClick(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        localMusicModeHandler.launch(intent)
    }

    fun onlineMusicClick(view: View) {}
    fun tutorialMusicClick(view: View) {}
}