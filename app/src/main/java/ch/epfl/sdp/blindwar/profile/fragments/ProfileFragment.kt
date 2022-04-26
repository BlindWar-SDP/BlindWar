package ch.epfl.sdp.blindwar.profile.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.login.SplashScreenActivity
import ch.epfl.sdp.blindwar.login.UserNewInfoActivity
import ch.epfl.sdp.blindwar.profile.model.User
import ch.epfl.sdp.blindwar.user.UserCache
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

/**
 * Fragment that displays a connected user info
 * TODO: CodeClimate issues / Cirrus warnings
 *
 * @constructor creates a ProfileFragment
 */
class ProfileFragment : Fragment(), UserCache {
    // DATABASE
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase

    // USER
    private var user = User()

    // BUTTONS
    private lateinit var statsButton: Button
    private lateinit var editButton: Button
    private lateinit var logOutButton: Button

    // TEXT VIEW
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var eloTextView: TextView

    private fun downloadImage() {
        if (user.profilePicture.isNotEmpty()) { // not default value
            imageDatabase.downloadProfilePicture(
                user.profilePicture,
                view?.findViewById(R.id.profileImageView)!!,
                activity?.applicationContext!!
            )
        }
    }

    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val userDB: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            userDB?.let {
                user = it
            }
            setView()
            downloadImage()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_profile, container, false)
        // Text View
        nameTextView = view.findViewById(R.id.nameView)
        emailTextView = view.findViewById(R.id.emailView)
        eloTextView = view.findViewById(R.id.eloDeclarationView)

        // Buttons
        statsButton = view.findViewById<Button>(R.id.statsButton).apply {
            this.setOnClickListener {
                /**
                TODO: debug StatisticsFragment
                activity?.supportFragmentManager?.beginTransaction()
                ?.replace((view?.parent as ViewGroup).id,
                StatisticsFragment(),
                "STATS")
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ?.commit()
                 **/

                val intent = Intent(requireActivity(), StatisticsActivity::class.java)
                startActivity(intent)
            }
        }

        editButton = view.findViewById<Button>(R.id.editProfileButton).apply {
            setButtonListener(this) { editProfile() }
        }


        logOutButton = view.findViewById<Button>(R.id.logoutButton).apply{
            setButtonListener(this) { logOut() }
        }

        // user id should be set according to authentication
        if (isOffline(activity?.applicationContext!!)) {
            user = readCache(activity?.applicationContext!!)
            Log.i("thco #######", user.pseudo)
            setView()
        } else {
            // user id should be set according to authentication
            FirebaseAuth.getInstance().currentUser?.let {
                database.addUserListener(it.uid, userInfoListener)
            }
        }
//        activity?.setContentView(R.layout.activity_profile) // ?

        return view
    }

    /**
     * Sets the button listener
     *
     * @param button
     * @param action performed on click
     */
    private fun setButtonListener(button: Button, action: () -> Unit) {
        button.setOnClickListener {
            action()
        }
    }

    /**
     * Opens profile edition activity
     */
    private fun editProfile() {
        startActivity(Intent(requireActivity(), UserNewInfoActivity::class.java))
    }

    /**
     * Handle the logout logic
     */
    private fun logOut() {
        // TODO : add warning for offline logout (lost of userinfo update)
        // TODO : same on backpressed on MainMenuActivity ?
        FirebaseAuth.getInstance().signOut()
        removeCache(activity?.applicationContext!!)
        startActivity(Intent(requireActivity(), SplashScreenActivity::class.java))
    }

    /**
     * set the text view to user's info
     */
    private fun setView() {
        nameTextView.text = user.firstName
        emailTextView.text = user.email
        eloTextView.text = user.userStatistics.elo.toString()
    }
}