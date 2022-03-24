package ch.epfl.sdp.blindwar.ui.solo

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import ch.epfl.sdp.blindwar.R


class SoloMenuActivity : AppCompatActivity() {

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
            val uri = intent?.data

            if(uri != null){
                val pickedDir = DocumentFile.fromTreeUri(this, uri)
                val builder = StringBuilder()
                for (file in pickedDir!!.listFiles()) {
                    builder.append(
                        """${file.name} -> ${file.length()}"""
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solo_menu)
    }

    fun yourMusicClick(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startForResult.launch(intent)
    }
    fun onlineMusicClick(view: View) {}
    fun tutorialMusicClick(view: View) {}
}