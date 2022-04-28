package ch.epfl.sdp.blindwar.profile.fragments

import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.login.SplashScreenActivity
import ch.epfl.sdp.blindwar.login.UserNewInfoActivity
import ch.epfl.sdp.blindwar.profile.HistoryActivity
import ch.epfl.sdp.blindwar.profile.model.User
import com.firebase.ui.auth.AuthUI
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
class ProfileFragment : Fragment() {
    // DATABASE
    private val database = UserDatabase
    private val imageDatabase = ImageDatabase
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    // BUTTONS
    private lateinit var statsButton: Button
    private lateinit var historyButton: Button
    private lateinit var deleteButton: Button
    private lateinit var editButton: Button
    private lateinit var logOutButton: Button

    private val userInfoListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get User info and use the values to update the UI
            val user: User? = try {
                dataSnapshot.getValue<User>()
            } catch (e: DatabaseException) {
                null
            }
            val nameView = view?.findViewById<TextView>(R.id.nameView)
            val emailView = view?.findViewById<TextView>(R.id.emailView)
            val eloView = view?.findViewById<TextView>(R.id.eloDeclarationView)
            val profileImageView = view?.findViewById<ImageView>(R.id.profileImageView)

            if (user != null) {
                nameView?.text = user.firstName
                emailView?.text = user.email
                eloView?.text = user.userStatistics.elo.toString()

                val imagePath = user.profilePicture.toString()
                if (imagePath != "" && profileImageView != null) {
                    imageDatabase.downloadProfilePicture(
                        imagePath,
                        profileImageView!!,
                        activity?.applicationContext!!
                    )
                }
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }

    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)
        // user id should be set according to authentication
        if (currentUser != null) {
            database.addUserListener(currentUser.uid, userInfoListener)
        }

        statsButton = view.findViewById<Button>(R.id.statsButton).apply {
            this.setOnClickListener{
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

        historyButton = view.findViewById<Button>(R.id.historyButton).apply{
            this.setOnClickListener{
                val intent = Intent(requireActivity(), HistoryActivity::class.java)
                startActivity(intent)
            }
        }

        deleteButton = view.findViewById<Button>(R.id.deleteProfile).apply {
            setButtonListener(this) { deleteProfile() }
        }

        editButton = view.findViewById<Button>(R.id.editProfileButton).apply {
            setButtonListener(this) { editProfile() }
        }

        logOutButton = view.findViewById<Button>(R.id.logoutButton).apply {
            setButtonListener(this) { logOut() }
        }

        return view
    }

    /**
     * Sets the button listener
     *
     * @param button
     * @param action performed on click
     */
    private fun setButtonListener(button: Button, action: () -> Unit) {
        button.setOnClickListener{
            action()
        }
    }

    /**
     * Opens profile edition activity
     */
    private fun editProfile() {
        //startActivity(Intent(requireActivity(), UserNewInfoActivity::class.java))
        val intent = Intent(requireActivity(), UserNewInfoActivity::class.java)
        intent.putExtra("activity", "profile")
        startActivity(intent)
    }

    /**
     * Handle the logout logic
     */
    private fun logOut() {
        auth.signOut()
        startActivity(Intent(requireActivity(), SplashScreenActivity::class.java))
//        AuthUI.getInstance().signOut(this).addOnCompleteListener {
//            startActivity(Intent(this, SplashScreenActivity::class.java))
//        }
    }

    /**
     * Handle the profile deletion logic
     */
    private fun deleteProfile() {
        // Alert Dialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        val positiveButtonClick = { _: DialogInterface, _: Int ->
            // new Alert Dialogue to ensure deletion
            val builderSecond: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            val secondPositiveButtonClick = { _: DialogInterface, _: Int ->

                currentUser?.let{UserDatabase.removeUser(it.uid)}
                AuthUI.getInstance().delete(requireActivity()).addOnCompleteListener {
                    startActivity(Intent(requireActivity(), SplashScreenActivity::class.java))
                }

                Toast.makeText(
                    requireActivity(),
                    getString(R.string.deletion_success), Toast.LENGTH_SHORT
                ).show()
            }
            val secondNegativeButtonClick = { _: DialogInterface, _: Int ->
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.account_not_deleted_confirm_toast), Toast.LENGTH_SHORT
                ).show()
            }

            builderSecond.setTitle(getString(R.string.account_deletion_confirm_title))
                .setMessage(getString(R.string.account_deletion_confirm_text))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, secondPositiveButtonClick)
                .setNegativeButton(android.R.string.cancel, secondNegativeButtonClick)
            builderSecond.create().show()

        }

        val negativeButtonClick = { _: DialogInterface, _: Int ->
            Toast.makeText(
                requireActivity(),
                getString(R.string.account_not_deleted_toast), Toast.LENGTH_SHORT
            ).show()
        }

        builder.setTitle(getString(R.string.account_deletion_title))
            .setMessage(getString(R.string.account_deletion_text))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, positiveButtonClick)
            .setNegativeButton(android.R.string.cancel, negativeButtonClick)
        builder.create().show()
    }
}