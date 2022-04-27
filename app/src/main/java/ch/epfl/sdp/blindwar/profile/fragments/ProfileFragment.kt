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
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.GlideApp
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.login.SplashScreenActivity
import ch.epfl.sdp.blindwar.login.UserNewInfoActivity
import ch.epfl.sdp.blindwar.profile.model.User
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.StorageReference

/**
 * Fragment that displays a connected user info
 * TODO: CodeClimate issues / Cirrus warnings
 *
 * @constructor creates a ProfileFragment
 */
class ProfileFragment : Fragment() {
    // DATABASE
    private val imageDatabase = ImageDatabase
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private val profileViewModel: ProfileViewModel by viewModels()

    // BUTTONS
    private lateinit var statsButton: Button
    private lateinit var deleteButton: Button
    private lateinit var editButton: Button
    private lateinit var logOutButton: Button
    private lateinit var optionsButton: Button
    private lateinit var optionsMenu: NavigationView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        // user id should be set according to authentication

        statsButton = view.findViewById<Button>(R.id.statsBtn).apply {
            this.setOnClickListener{
                val intent = Intent(requireActivity(), StatisticsActivity::class.java)
                startActivity(intent)
            }
        }

        view.findViewById<ImageButton>(R.id.editBtn).apply {
            this.setOnClickListener{
                editProfile()
            }
        }

        view.findViewById<ImageButton>(R.id.logoutBtn).apply {
            this.setOnClickListener{
                logOut()
            }
        }

        view.findViewById<ImageButton>(R.id.deleteBtn).apply {
            this.setOnClickListener{
                deleteProfile()
            }
        }

        observeUserValue(profileViewModel.name, view.findViewById(R.id.nameView))
        //observeUserValue(profileViewModel.email, view.findViewById(R.id.emailView))
        observeUserValue(profileViewModel.elo, view.findViewById(R.id.eloView))
        updateProfileImage(profileViewModel.imageRef, view.findViewById(R.id.profileImgView))

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
        startActivity(Intent(requireActivity(), UserNewInfoActivity::class.java))
    }

    /**
     *
     */
    private fun optionsBtn() {
        optionsMenu.visibility =
            if (optionsMenu.visibility == View.VISIBLE) View.GONE
            else View.VISIBLE
    }

    /**
     * Handle the logout logic
     */
    private fun logOut() {
        auth.signOut()
        startActivity(Intent(requireActivity(), SplashScreenActivity::class.java))
        /**
            AuthUI.getInstance().signOut(this).addOnCompleteListener {
            startActivity(Intent(this, SplashScreenActivity::class.java))
        }
        **/
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

    // OBSERVABLES
    private fun observeUserValue(liveData: LiveData<String>, view: TextView) {
        liveData.observe(viewLifecycleOwner){
            view.text = it
        }
    }

    private fun updateProfileImage(liveData: LiveData<StorageReference>, imageView: ImageView) {
        liveData.observe(viewLifecycleOwner) {
            if (it.path != "") {
                GlideApp.with(requireActivity())
                    .load(it)
                    .centerCrop()
                    .into(imageView)
            }
        }
    }
}