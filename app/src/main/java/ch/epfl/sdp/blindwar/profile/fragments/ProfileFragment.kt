package ch.epfl.sdp.blindwar.profile.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.util.Util.loadProfileImage
import ch.epfl.sdp.blindwar.login.SplashScreenActivity
import ch.epfl.sdp.blindwar.profile.HistoryActivity
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Fragment that displays a connected user info
 * TODO: CodeClimate issues / Cirrus warnings
 *
 * @constructor creates a ProfileFragment
 */
class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Buttons
        val btnStat = view.findViewById<Button>(R.id.statsBtn)
        btnStat.setOnClickListener {
            startActivity(Intent(requireContext(), StatisticsActivity::class.java))
        }
        val btnHistory = view.findViewById<Button>(R.id.historyBtn)
        btnHistory.setOnClickListener {
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }
        val btnEdit = view.findViewById<ImageButton>(R.id.editBtn)
        btnEdit.setOnClickListener {
            editProfile()
        }
        val btnLogout = view.findViewById<ImageButton>(R.id.logoutBtn)
        btnLogout.setOnClickListener {
            logOut()
        }
        val btnDelete = view.findViewById<ImageButton>(R.id.deleteBtn)
        btnDelete.setOnClickListener {
            deleteProfile()
        }

        // text view
        profileViewModel.user.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.nameView).text = it.pseudo
            view.findViewById<TextView>(R.id.eloView).text = it.userStatistics.elo.toString()
        }

        // profilePicture
        loadProfileImage(
            profileViewModel.imageRef,
            view.findViewById(R.id.profileImgView),
            viewLifecycleOwner,
            requireContext()
        )

        return view
    }


    /**
     * Opens profile edition activity
     */
    private fun editProfile() {
        fragmentManager?.let {
            it.beginTransaction().apply {
                replace(R.id.fragment_menu_container, UserNewInfoFragment())
                commit()
            }
        }
    }

    /**
     * Handle the logout logic
     */
    private fun logOut() {
        profileViewModel.logout()
        startActivity(
            Intent(requireContext(), SplashScreenActivity::class.java)
        )
    }

    /**
     * Handle the profile deletion logic
     */
    private fun deleteProfile() {
        // Alert Dialog
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.account_deletion_title))
            .setMessage(getString(R.string.account_deletion_text))
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok, positiveButtonClick)
            .setNegativeButton(android.R.string.cancel, negativeButtonClick)
            .create()
            .show()
    }

    /**
     * delete profile and sign out auth
     */
    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        Firebase.auth.currentUser?.let {
            UserDatabase.removeUser(it.uid)
            AuthUI.getInstance().delete(requireContext()).addOnCompleteListener {
                startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
            }
            Toast.makeText(
                requireContext(),
                getString(R.string.deletion_success), Toast.LENGTH_SHORT
            ).show()
        } ?: run {
            Toast.makeText(
                requireContext(), "something went wrong on deletion", Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(requireContext(), SplashScreenActivity::class.java))
        }
    }

    /**
     * show toast if deletion is cancelled
     */
    private val negativeButtonClick = { _: DialogInterface, _: Int ->
        Toast.makeText(
            requireContext(),
            getString(R.string.account_not_deleted_toast), Toast.LENGTH_SHORT
        ).show()
    }
}