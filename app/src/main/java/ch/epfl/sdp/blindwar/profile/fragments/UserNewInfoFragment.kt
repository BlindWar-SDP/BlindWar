package ch.epfl.sdp.blindwar.profile.fragments

//import ch.epfl.sdp.blindwar.user.UserCache
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.GlideApp
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.login.SplashScreenActivity
import ch.epfl.sdp.blindwar.login.viewmodel.UserNewInfoViewModel
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.model.Gender
import ch.epfl.sdp.blindwar.profile.model.User
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import java.util.*


/**
 * Activity that let the user enter its principal information when registering for the app
 * TODO: fix CodeClimate issues / fix Cirrus warnings when possible
 *
 * @constructor creates a UserNewInfoActivity
 */
class UserNewInfoFragment : Fragment()/*, UserCache*/ {

    private val userNewInfoViewModel: UserNewInfoViewModel by activityViewModels()
    private var localPPuri: Uri? = null

    /**
     * Makes sure data is ok, before launching
     */
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    it.data?.let { uri ->
                        localPPuri = uri
                        showImage()
                    }
                }
            }
        }

    /**
     * Generates the layout and adds listener for current user
     *
     * @param savedInstanceState
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_new_info, container, false)

        userNewInfoViewModel.userMLD.observe(viewLifecycleOwner) {
            if (it.profilePicture.isEmpty()) {
                hideProfilePicture()
            }
            setView(it)
        }

        // Buttons
        view.findViewById<Button>(R.id.NU_deleteProfile).apply {
            this.setOnClickListener { deleteProfile() }
        }
        view.findViewById<Button>(R.id.NU_editProfilePicture).apply {
            this.setOnClickListener { setPicture() }
        }
        view.findViewById<Button>(R.id.NU_resetProfilePicture).apply {
            this.setOnClickListener { resetPicture() }
        }
        view.findViewById<Button>(R.id.NU_select_birthdate).apply {
            this.setOnClickListener { setBirthdate() }
        }
        view.findViewById<Button>(R.id.NU_reset_birthdate).apply {
            this.setOnClickListener { resetBirthdate() }
        }
        view.findViewById<Button>(R.id.NU_Confirm_Btn).apply {
            this.setOnClickListener { confirm() }
        }

        return view
    }

    /**
     * To avoid crashing the app, if the back button is pressed, user will log out
     *
     */
//    override fun onBackPressed() {
//        //this.moveTaskToBack(true)
//        val intent = intent
//        val activity = intent.getStringExtra("activity")
//        //Log.w("yeet", activity!!)
//        if (activity != "profile") {
//            AuthUI.getInstance().delete(this)
//            FirebaseAuth.getInstance().signOut()
//            startActivity(Intent(this, SplashScreenActivity::class.java))
//        } else {
//            super.onBackPressed()
//        }
//    }

    /**
     * Function for confirming and saving all info entered by user
     *
     * @param v
     */
   private fun confirm() {
        setFromText()

        userNewInfoViewModel.userMLD.observe(viewLifecycleOwner) {
            // check validity of pseudo
            if (it.pseudo.length < resources.getInteger(R.integer.pseudo_minLength)) {
                // Alert Dialog
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                val positiveButtonClick = { _: DialogInterface, _: Int -> }
                builder.setTitle(R.string.new_user_wrong_pseudo_title)
                    .setMessage(R.string.new_user_wrong_pseudo_text)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, positiveButtonClick)
                builder.create().show()
            } else {
                userNewInfoViewModel.uploadImage(localPPuri)
//            writeCache(this, user)
                UserDatabase.updateUser(it)
                startActivity(Intent(requireActivity(), MainMenuActivity::class.java))
            }
        }
    }

    private fun setPicture() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private fun resetPicture() {
        userNewInfoViewModel.resetPicture()
        localPPuri = null
        hideProfilePicture()
        showImage()
    }

    private fun setBirthdate() {
        val calendar: Calendar = Calendar.getInstance() // current date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        calendar.add(Calendar.YEAR, -resources.getInteger(R.integer.age_min))
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(
                requireActivity(),
                { _, mYear, mMonth, mDay ->
                    setBirthdateText(
                        userNewInfoViewModel.setDate(mYear, mMonth, mDay)
                    )
                },
                year,
                month,
                day
            )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -resources.getInteger(R.integer.age_max))
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.setIcon(R.drawable.logo)
        datePickerDialog.setTitle(R.string.new_user_birthdatePicker)
        datePickerDialog.show()
    }

    private fun resetBirthdate() {
        view?.let {
            userNewInfoViewModel.resetBirthdate()
            val textView = it.findViewById<TextView>(R.id.NU_selected_birthdate_text)
            textView.text = resources.getString(R.string.no_birthdate_selected)
            it.findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.INVISIBLE
        }
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

                FirebaseAuth.getInstance().currentUser?.let {
                    UserDatabase.removeUser(it.uid)
                    AuthUI.getInstance().delete(requireActivity()).addOnCompleteListener {
                        startActivity(Intent(requireActivity(), SplashScreenActivity::class.java))
                    }
//                    removeCache(this)
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.deletion_success), Toast.LENGTH_SHORT
                    ).show()
                } ?: run {
                    Toast.makeText(
                        requireActivity(),
                        "something went wrong on deletion", Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(requireActivity(), SplashScreenActivity::class.java))
                }
            }
            val secondNegativeButtonClick = { _: DialogInterface, _: Int ->
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.account_not_deleted_confirm_toast) + "🥳",
                    Toast.LENGTH_SHORT
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

    // =============================================================================================
    // ================================== HELPER FUNCTIONS ========================================
    // =============================================================================================

    private fun hideButton(id: Int) {
        view?.let {
            it.findViewById<Button>(id).visibility = View.INVISIBLE
        }
    }

    private fun hideProfilePicture() {
        view?.let {
            it.findViewById<ImageView>(R.id.NU_profileImageView).setImageResource(
                android.R.color.transparent
            )
            hideButton(R.id.NU_resetProfilePicture)
        }
    }

    private fun setFromText() {
        view?.let {
            val pseudo = it.findViewById<EditText>(R.id.NU_pseudo).text.toString()
            val firstName = it.findViewById<EditText>(R.id.NU_FirstName).text.toString()
            val lastName = it.findViewById<EditText>(R.id.NU_LastName).text.toString()
            val description = it.findViewById<EditText>(R.id.NU_description).text.toString()
            userNewInfoViewModel.setMLDFromText(pseudo, firstName, lastName, description)
        }
    }

    private fun showImage() {
        view?.let { v ->
            localPPuri?.let { uri ->
                v.findViewById<ImageView>(R.id.NU_profileImageView).setImageURI(uri)
                v.findViewById<Button>(R.id.NU_resetProfilePicture).visibility = View.VISIBLE
            } ?: run {
                downloadImage()
            }
        }
    }

    private fun setView(user: User) {
        view?.let { v ->
            v.findViewById<EditText>(R.id.NU_pseudo).setText(user.pseudo)
            v.findViewById<EditText>(R.id.NU_FirstName).setText(user.firstName)
            v.findViewById<EditText>(R.id.NU_LastName).setText(user.lastName)
            showImage()
            if (user.birthdate != User().birthdate) {
                setBirthdateText(user.birthdate)
                v.findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.VISIBLE
            } else {
                v.findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.INVISIBLE
            }
            v.findViewById<TextView>(R.id.NU_description).text = user.description


            // Spinner for GENDER
            v.findViewById<Spinner>(R.id.gender_spinner)?.let { spinner ->
                val adapter = ArrayAdapter(
                    requireActivity(),
                    R.layout.spinner_item,
                    Gender.values()
                )

//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                if (user.gender.isNotEmpty()) {
                    // because default value for gender is "", which is not in the Enum Class
                    spinner.setSelection(Gender.valueOf(user.gender).ordinal)
                } else {
                    spinner.setSelection(adapter.count - 1)
                }
                spinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long
                    ) {
                        val chosen = parent.getItemAtPosition(position)
                        user.gender = if (chosen == Gender.None) {
                            ""
                        } else {
                            chosen.toString()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        user.gender = ""
                    }
                }
            }
        }
    }

    private fun downloadImage() {
        view?.let { v ->
            userNewInfoViewModel.imageRef.observe(viewLifecycleOwner) {
                it?.let{
                    GlideApp.with(requireActivity())
                        .load(it)
                        .centerCrop()
                        .into(v.findViewById(R.id.NU_profileImageView))
                }
            }
        }
    }

    private fun setBirthdateText(birthdate: Long) {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = birthdate
        view?.let {
            val textView = it.findViewById<TextView>(R.id.NU_selected_birthdate_text)
            val textStr = "birthdate set to\n" +
                    "${cal.get(Calendar.DAY_OF_MONTH)}/" +
                    "${cal.get(Calendar.MONTH) + 1}/" +
                    "${cal.get(Calendar.YEAR)}"
            textView.text = textStr
            it.findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.VISIBLE
        }
    }

}
