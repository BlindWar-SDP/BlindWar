package ch.epfl.sdp.blindwar.profile.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.GlideApp
import ch.epfl.sdp.blindwar.login.SplashScreenActivity
import ch.epfl.sdp.blindwar.login.UserNewInfoActivity
import ch.epfl.sdp.blindwar.profile.HistoryActivity
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import com.google.firebase.storage.StorageReference

/**
 * Fragment that displays a connected user info
 * TODO: CodeClimate issues / Cirrus warnings
 *
 * @constructor creates a ProfileFragment
 */
class ProfileFragment : Fragment() {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    // BUTTONS
    private lateinit var statsButton: Button
    private lateinit var historyButton: Button
    private lateinit var editButton: Button
    private lateinit var logOutButton: Button

    // TEXT VIEW
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var eloTextView: TextView

    /**
     * download image from database and show it

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
    }**/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Buttons
        statsButton = view.findViewById<Button>(R.id.statsBtn).apply {
            this.setOnClickListener {
                val intent = Intent(requireActivity(), StatisticsActivity::class.java)
                startActivity(intent)
            }
        }

        view.findViewById<ImageButton>(R.id.editBtn).apply {
            this.setOnClickListener {
                editProfile()
            }
        }

        view.findViewById<ImageButton>(R.id.logoutBtn).apply {
            this.setOnClickListener {
                logOut()
            }
        }

        historyButton = view.findViewById<Button>(R.id.historyBtn).apply {
            this.setOnClickListener {
                val intent = Intent(requireActivity(), HistoryActivity::class.java)
                startActivity(intent)
            }
        }

        view.findViewById<ImageButton>(R.id.deleteBtn).apply {
            this.setOnClickListener {
                //deleteProfile()
            }
        }

        observeUserValue(profileViewModel.name, view.findViewById(R.id.nameView))
        //observeUserValue(profileViewModel.email, view.findViewById(R.id.emailView))
        observeUserValue(profileViewModel.elo, view.findViewById(R.id.eloView))
        updateProfileImage(
            profileViewModel.imageRef,
            view.findViewById(R.id.profileImgView)
        )

        /**
        // TODO: Move cache in User repository
        // user id should be set according to authentication
        if (isOffline(activity?.applicationContext!!)) {
        user = readCache(activity?.applicationContext!!)
        //setView()
        } else {
        // user id should be set according to authentication
        FirebaseAuth.getInstance().currentUser?.let {
        //database.addUserListener(it.uid, userInfoListener)
        }
        }
         **/

        return view
    }


    /**
     * Opens profile edition activity
     */
    private fun editProfile() {
        val intent = Intent(requireActivity(), UserNewInfoActivity::class.java)
        intent.putExtra("activity", "profile")
        startActivity(intent)
    }

    /**
     * Handle the logout logic
     */
    private fun logOut() {
        // TODO : add warning for offline logout (lost of userinfo update)
        // TODO : same on backpressed on MainMenuActivity ?
        profileViewModel.logout()
        //removeCache(activity?.applicationContext!!)
        startActivity(Intent(requireActivity(), SplashScreenActivity::class.java))
    }

    // OBSERVABLES
    private fun observeUserValue(liveData: LiveData<String>, view: TextView) {
        liveData.observe(viewLifecycleOwner) {
            view.text = it
        }
    }

    private fun updateProfileImage(
        liveData: LiveData<StorageReference>,
        imageView: ImageView
    ) {
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