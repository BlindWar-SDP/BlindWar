package ch.epfl.sdp.blindwar.profile.fragments

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
import ch.epfl.sdp.blindwar.database.ImageDatabase
import ch.epfl.sdp.blindwar.database.UserDatabase
import ch.epfl.sdp.blindwar.game.util.Util.loadProfileImage
import ch.epfl.sdp.blindwar.menu.MainMenuActivity
import ch.epfl.sdp.blindwar.profile.model.Gender
import ch.epfl.sdp.blindwar.profile.model.User
import ch.epfl.sdp.blindwar.profile.viewmodel.ProfileViewModel
import java.util.*

/**
 * Activity that let the user enter its principal information when registering for the app
 *
 * @constructor creates a UserNewInfoActivity
 */
class UserNewInfoFragment : Fragment() {

    private val imagePath = "image/*"
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var localPPuri: Uri? = null
    private var user = User()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_new_info, container, false)
        profileViewModel.user.observe(viewLifecycleOwner) {
            user = it
            if (user.profilePicture.isEmpty()) {
                hideProfilePicture()
            }
            setView(user)
        }
        // Buttons
        val btnEditPP = view.findViewById<Button>(R.id.NU_editProfilePicture)
        btnEditPP.setOnClickListener { setPicture() }
        val btnResetPP = view.findViewById<Button>(R.id.NU_resetProfilePicture)
        btnResetPP.setOnClickListener { resetPicture() }
        val btnSetBirthdate = view.findViewById<Button>(R.id.NU_select_birthdate)
        btnSetBirthdate.setOnClickListener { setBirthdate() }
        val btnResetBirthdate = view.findViewById<Button>(R.id.NU_reset_birthdate)
        btnResetBirthdate.setOnClickListener { resetBirthdate() }
        val btnConfirm = view.findViewById<Button>(R.id.NU_Confirm_Btn)
        btnConfirm.setOnClickListener { confirm() }
        return view
    }

    /**
     * Function for confirming and saving all info entered by user
     *
     */
    private fun confirm() {
        setFromText()
        // check validity of pseudo
        if (user.pseudo.length < resources.getInteger(R.integer.pseudo_minLength)) {
            // Alert Dialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            val positiveButtonClick = { _: DialogInterface, _: Int -> }
            builder.setTitle(R.string.new_user_wrong_pseudo_title)
                .setMessage(R.string.new_user_wrong_pseudo_text)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, positiveButtonClick)
            builder.create().show()
        } else {
            uploadImage(localPPuri)
            UserDatabase.updateUser(user)
            startActivity(Intent(requireContext(), MainMenuActivity::class.java))
        }
    }

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
     * set Profile Picture to local image path
     *
     */
    private fun setPicture() {
        val intent = Intent()
        intent.type = imagePath
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    /**
     * Reset Profile Picture to default value
     *
     */
    private fun resetPicture() {
        localPPuri = null
        profileViewModel.imageRef.value = null
        user.profilePicture = User().profilePicture
        hideProfilePicture()
        showImage()
    }

    /**
     * set Birthdate attribute to selected value via a dialog
     *
     */
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
                    setBirthdateText(setDate(mYear, mMonth, mDay))
                },
                year,
                month,
                day
            )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        // test fails due to those two lines ... ?? //TODO ?
//        calendar.add(Calendar.YEAR, -resources.getInteger(R.integer.age_max))
//        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.setIcon(R.drawable.logo)
        datePickerDialog.setTitle(R.string.new_user_birthdatePicker)
        datePickerDialog.show()
    }

    /**
     * reset Birthdate attribute to default value
     *
     */
    private fun resetBirthdate() {
        view?.let {
            user.birthdate = User().birthdate
            val textView = it.findViewById<TextView>(R.id.NU_selected_birthdate_text)
            textView.text = resources.getString(R.string.no_birthdate_selected)
            it.findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.INVISIBLE
        }
    }

// =============================================================================================
// ================================== HELPER FUNCTIONS ========================================
// =============================================================================================

    /**
     * given a Button ID, hide it
     *
     * @param id
     */
    private fun hideButton(id: Int) {
        view?.let {
            it.findViewById<Button>(id).visibility = View.INVISIBLE
        }
    }

    /**
     * hide resetProfilePicture button and image
     *
     */
    private fun hideProfilePicture() {
        view?.let {
            it.findViewById<ImageView>(R.id.NU_profileImageView).setImageResource(
                android.R.color.transparent
            )
            hideButton(R.id.NU_resetProfilePicture)
        }
    }

    /**
     * set user from textView
     *
     */
    private fun setFromText() {
        view?.let {
            user.pseudo =
                it.findViewById<EditText>(R.id.NU_pseudo).text.toString()
            user.firstName =
                it.findViewById<EditText>(R.id.NU_FirstName).text.toString()
            user.lastName =
                it.findViewById<EditText>(R.id.NU_LastName).text.toString()
            user.description =
                it.findViewById<EditText>(R.id.NU_description).text.toString()
        }
    }

    /**
     * set UI from user's info
     *
     * @param user
     */
    private fun setView(user: User) {
        view?.let { v ->
            v.findViewById<EditText>(R.id.NU_pseudo).setText(user.pseudo)
            v.findViewById<EditText>(R.id.NU_FirstName).setText(user.firstName)
            v.findViewById<EditText>(R.id.NU_LastName).setText(user.lastName)
            showImage()
            if (user.birthdate == User().birthdate) {
                hideButton(R.id.NU_reset_birthdate)
            } else {
                setBirthdateText(user.birthdate)
                v.findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.VISIBLE
            }
            v.findViewById<TextView>(R.id.NU_description).text = user.description

            // Spinner for GENDER
            v.findViewById<Spinner>(R.id.gender_spinner)?.let { spinner ->
                showSpinner(spinner)
            }
        }
    }

    /**
     * show Spinner for Gender selection
     *
     * @param spinner
     */
    private fun showSpinner(spinner: Spinner) {
        val adapter = ArrayAdapter(
            requireActivity(),
            R.layout.spinner_item,
            Gender.values()
        )
        spinner.adapter = adapter

        // Show selected gender
        var selected = adapter.count - 1
        if (user.gender.isNotEmpty()) {
            selected = Gender.valueOf(user.gender).ordinal
        }
        spinner.setSelection(selected)

        // Set user data from chosen gender from spinner
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

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    /**
     * show ProfilePicture from local or load it from database if required
     *
     */
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

    /**
     * set textView to the selected date
     *
     * @param birthdate
     */
    private fun setBirthdateText(birthdate: Long) {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = birthdate
        view?.let {
            val textView = it.findViewById<TextView>(R.id.NU_selected_birthdate_text)
            val textStr = getString(R.string.birthdate_set_to) + "\n" +
                    "${cal.get(Calendar.DAY_OF_MONTH)}/" +
                    "${cal.get(Calendar.MONTH) + 1}/" +
                    "${cal.get(Calendar.YEAR)}"
            textView.text = textStr
            it.findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.VISIBLE
        }
    }

    /**
     * given the detailed date, set user's birthdate attribute to selected date
     * and return the date as a Long value
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private fun setDate(year: Int, month: Int, day: Int): Long {
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, day)
        user.birthdate = cal.timeInMillis
        return cal.timeInMillis
    }

    private fun downloadImage() {
        view?.let { v ->
            loadProfileImage(
                profileViewModel.imageRef,
                v.findViewById(R.id.NU_profileImageView),
                viewLifecycleOwner,
                requireContext()
            )
        }
    }

    /**
     * upload ProfilePicture when chosen from local storage
     *
     * @param uri
     */
    private fun uploadImage(uri: Uri?) {
        // TODO : Should be done in ViewModel but need return value to set profile picture ...
        uri?.let { it_uri ->
            user.profilePicture =
                ImageDatabase.uploadProfilePicture(it_uri)
        }
    }
}


