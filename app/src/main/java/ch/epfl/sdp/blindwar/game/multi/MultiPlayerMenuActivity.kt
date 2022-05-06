package ch.epfl.sdp.blindwar.game.multi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.MatchDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


/**
 * Activity that lets the user start a multiplayer game
 *
 * @constructor creates a MultiPlayerMenuActivity
 */
class MultiPlayerMenuActivity : AppCompatActivity() {
    private var eloDelta = 200
    private var dialog: AlertDialog? = null
    private var isCanceled = false
    private lateinit var toast: Toast

    companion object {
        private val LIMIT_MATCH: Long = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer_menu)
        toast = Toast.makeText(applicationContext, "default", Toast.LENGTH_SHORT)
        eloDelta = 200
    }

    /**
     * Starts a multiplayer game played with a friend
     *
     * @param view
     */
    fun friendButton(view: View) {
        setLinkDialog()
        //dialog!!.hide() //TODO REMOVE WHEN TESTS OK
    }

    /**
     * Launch the create match activity
     *
     * @param view
     */
    fun createMatch(view: View) {
        startActivity(Intent(this, ChoseNumberOfPlayerActivity::class.java))
    }

    /**
     * Cancel
     *
     * @param view
     */
    fun cancel(view: View) {
        startActivity(Intent(this, MainMenuActivity::class.java))
    }

    /**
     * Starts a multiplayer game played with a random user
     *
     * @param view
     */
    fun randomButton(view: View) {
        setProgressDialog(getString(R.string.multi_wait_matches))
        dialog!!.hide() //TODO REMOVE WHEN TESTS OK
        //val intent = Intent(this, MultiPlayerRandomActivity::class.java)
    }

    /**
     * display progressDialog cancelable for any messages
     *
     * @param message
     */
    private fun setProgressDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setOnCancelListener {
            isCanceled = true
            toast.setText(getString(R.string.toast_canceled_connexion))
            toast.show()
        }
        val view = View.inflate(applicationContext, R.layout.fragment_dialog_loading, null)
        builder.setView(view)
        (view.findViewById<TextView>(R.id.textView_multi_loading)).text = message
        dialog = builder.create()
        dialog!!.show()
    }
    

    /**
     * create a dialog which ask for the uid of the match
     *
     */
    private fun setLinkDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        val view = View.inflate(applicationContext, R.layout.fragment_multi_connexion_link, null)
        builder.setView(view)
        builder.setNeutralButton(resources.getText(R.string.cancel_btn)) { _, _ ->
            dialog!!.hide()
        }
        builder.setPositiveButton(resources.getText(R.string.ok)) { _, _ ->
            //connectToDB(findViewById<EditText>(R.id.editTextLink).text.toString())
            dialog!!.hide()
        }
        dialog = builder.create()
        dialog!!.show()
    }
}
