package ch.epfl.sdp.blindwar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.tutorial.TutorialActivity
import com.squareup.picasso.Picasso

const val EXTRA_MESSAGE = "ch.epfl.blindwar.MESSAGE"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val url = "https://upload.wikimedia.org/wikipedia/en/5/55/Michael_Jackson_-_Thriller.png"
        var imageView = findViewById<ImageView>(R.id.imageView)
        Picasso.get().load(url).into(imageView)

        val tutorial = findViewById<Button>(R.id.tutoButton)
        val intent = Intent(this, TutorialActivity::class.java)
        tutorial.setOnClickListener{
            startActivity(intent)
        }
    }




    /**
    /** Called when the user taps the Send button */
    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val message = editText.text.toString()
        val intent = Intent(this, GreetingActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
    **/
}