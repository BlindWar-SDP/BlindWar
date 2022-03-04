package ch.epfl.sdp.blindwar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TutorialActivity : AppCompatActivity() {
    private val formatDescr = """Survival : Description placeholder
    |Against the Clock : 
    |Regular : Description placeholder""".trimMargin("|")

    private val modesDescr = """Solo Game : Description placeholder
    |Multi Game : Description placeholder""".trimMargin("|")

    private val commDescr = """Dedicated fragment describing the UI elements : 
    |Microphone / Pass / Timer / Settings""".trimMargin("|")

    private val optDescr = """Song Selection : Description placeholder
    |Altered audio : Description placeholder""".trimMargin("|")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        setTexts()
    }

    private fun setTexts() {
        findViewById<TextView>(R.id.modes_description).text = modesDescr
        findViewById<TextView>(R.id.format_description).text= formatDescr
        findViewById<TextView>(R.id.song_opt_description).text = optDescr
        findViewById<TextView>(R.id.commands_description).text = commDescr
    }
}