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
import ch.epfl.sdp.blindwar.login.*
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

    //    private val database = UserDatabase
//    private val imageDatabase = ImageDatabase
//    private var profilePictureUri: Uri? = null
//    private val auth = FirebaseAuth.getInstance()
    private var dynamicLink: String? = null

    private val imagePath = "image/*"
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private var localPPuri: Uri? = null
    private var user = User()

    /**
     * Listener for user entering new information
     *
    private val userInfoListener = object : ValueEventListener {
    override fun onDataChange(dataSnapshot: DataSnapshot) {
    // Get User info and use the values to update the UI
    val user: User? = try {
    dataSnapshot.getValue<User>()
    } catch (e: DatabaseException) {
    null
    }
    val firstName = findViewById<EditText>(R.id.NU_FirstName)
    val lastName = findViewById<EditText>(R.id.NU_LastName)
    val pseudo = findViewById<EditText>(R.id.NU_pseudo)
    val profileImageView = findViewById<ImageView>(R.id.NU_profileImageView)

    user?.let {
    firstName.setText(it.firstName)
    lastName.setText(it.lastName)
    pseudo.setText(it.pseudo)
    if (!intent.getBooleanExtra("newUser", false)) {
    if (it.profilePicture != "null") {
    // TODO: Refactor UserNewInfoActivity
    //imageDatabase.downloadProfilePicture(
    //  it.profilePicture!!,
    // profileImageView,
    // applicationContext
    //)
    }
    }
    }
    }

    override fun onCancelled(databaseError: DatabaseError) {
    // Getting Post failed, log a message
    // Log.w("CANCELED REQUEST", "loadPost:onCancelled", databaseError.toException())
    }
    }
     */

    /**
     * Generates the layout and adds listener for current user
     *
     * @param savedInstanceState

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_new_info)
    dynamicLink = intent?.extras?.getString(MultiPlayerMenuActivity.DYNAMIC_LINK) // TODO : DynLink
    // user id should be set according to authentication
    FirebaseAuth.getInstance().currentUser?.let {
    database.addUserListener(it.uid, userInfoListener)
    }
    }
     */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_new_info, container, false)
//        dynamicLink = intent?.extras?.getString(MultiPlayerMenuActivity.DYNAMIC_LINK) // TODO : DynLink

        profileViewModel.user.observe(viewLifecycleOwner) {
            user = it
            if (user.profilePicture.isEmpty()) {
                hideProfilePicture()
            }
            setView(user)
        }
        // Buttons
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
//        //this.moveTaskToBack(true);
//        val intent = intent
//        val activity = intent.getStringExtra("activity")
//        //Log.w("yeet", activity!!)
//        if (activity != "profile") {
//            AuthUI.getInstance().delete(this)
//            auth.signOut()
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
        // check validity of pseudo
//        Log.i(">>> User Pseudo", user.pseudo)
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
            startActivity(Intent(requireActivity(), MainMenuActivity::class.java))
        }
    }
    /**
    fun confirm(v: View) {
    val pseudo: String = findViewById<EditText>(R.id.NU_pseudo).text.toString()
    val firstName: String = checkNotDefault(
    findViewById<EditText>(R.id.NU_FirstName).text.toString(),
    R.string.first_name
    )
    val lastName: String = checkNotDefault(
    findViewById<EditText>(R.id.NU_LastName).text.toString(),
    R.string.last_name
    )
    val birthDate: Long = intent.getLongExtra("birthdate", -1)
    val profilePicture: String = profilePictureUri.toString()
    val gender = intent.getStringExtra("gender") ?: Gender.None.toString()
    val description = intent.getStringExtra("description") ?: ""
    val isNewUser = intent.getBooleanExtra("newUser", false)

    // check validity of pseudo
    if (pseudo.length < resources.getInteger(R.integer.pseudo_minLength) || pseudo == resources.getString(
    R.string.text_pseudo
    )
    ) {
    // Alert Dialog
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    val positiveButtonClick = { _: DialogInterface, _: Int ->
    }

    builder.setTitle(R.string.new_user_wrong_pseudo_title)
    .setMessage(R.string.new_user_wrong_pseudo_text)
    .setCancelable(false)
    .setPositiveButton(android.R.string.ok, positiveButtonClick)
    builder.create().show()

    // Or Toast
    //            Toast.makeText(this, R.string.new_user_wrong_pseudo_text, Toast.LENGTH_SHORT).show()

    } else {
    // check if new user or update already existing user
    if (isNewUser) {
    createUser(
    pseudo,
    firstName,
    lastName,
    birthDate,
    profilePicture,
    gender,
    description
    ) // TODO : Comment for TESTing -> need to uncomment
    //            AuthUI.getInstance().delete(this) // TODO : uncomment for TESTing
    if (dynamicLink == null) { // TODO : DynLink
    startActivity(Intent(this, MainMenuActivity::class.java))
    } else {
    startActivity(
    Intent(this, MultiPlayerMenuActivity::class.java).putExtra(
    MultiPlayerMenuActivity.DYNAMIC_LINK,
    dynamicLink // TODO : DynLink
    )
    )
    }
    } else {
    FirebaseAuth.getInstance().currentUser?.let {
    UserDatabase.setPseudo(it.uid, pseudo)
    UserDatabase.setFirstName(it.uid, firstName)
    UserDatabase.setLastName(it.uid, lastName)
    UserDatabase.setProfilePicture(it.uid, profilePicture)
    UserDatabase.setGender(it.uid, gender)
    UserDatabase.setBirthdate(it.uid, birthDate)
    UserDatabase.setDescription(it.uid, description)
    if (dynamicLink == null) { // TODO : DynLink
    startActivity(Intent(this, MainMenuActivity::class.java))
    } else {
    startActivity(
    Intent(this, MultiPlayerMenuActivity::class.java).putExtra(
    MultiPlayerMenuActivity.DYNAMIC_LINK,
    dynamicLink // TODO : DynLink
    )
    )
    }
    }
    }

    // Upload picture to database
    profilePictureUri?.let {
    imageDatabase.uploadProfilePicture(
    FirebaseAuth.getInstance().currentUser, it,
    findViewById(android.R.id.content)
    )
    }
    }
    }
     */

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
     * Lets the user choose their own profile picture
     *
     * @param v
     */
    fun choosePicture(v: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private fun setPicture() {
        val intent = Intent()
        intent.type = imagePath
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private fun resetPicture() {
        localPPuri = null
        profileViewModel.imageRef.value = null
        user.profilePicture = User().profilePicture
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
                    setBirthdateText(setDate(mYear, mMonth, mDay))
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
            user.birthdate = User().birthdate
            val textView = it.findViewById<TextView>(R.id.NU_selected_birthdate_text)
            textView.text = resources.getString(R.string.no_birthdate_selected)
            it.findViewById<Button>(R.id.NU_reset_birthdate).visibility = View.INVISIBLE
        }
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
                hideButton(R.id.NU_reset_birthdate)
            }
            v.findViewById<TextView>(R.id.NU_description).text = user.description

            // Spinner for GENDER
            v.findViewById<Spinner>(R.id.gender_spinner)?.let { spinner ->
                showSpinner(spinner)
            }
        }
    }

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

            override fun onNothingSelected(parent: AdapterView<*>) {
                user.gender = ""
            }
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

    private fun uploadImage(uri: Uri?) {
        // TODO : Should be done in ViewModel but need return value to set profile picture ...
        uri?.let { it_uri ->
            user.profilePicture =
                ImageDatabase.uploadProfilePicture(it_uri /*, requireContext()*/)
        }
    }
}


