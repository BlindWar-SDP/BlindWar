package ch.epfl.sdp.blindwar.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.user.User
import java.util.*

class UserAdditionalInfoActivity : AppCompatActivity() {

    private var gender: String? = ""
    private var genderPosition = User.Gender.None.ordinal
    private var description : String? = ""
    private var birthdate: Long = -1

    private var minAge = -1
    private var maxAge = -1
    private var isNewUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_additional_info)

        // cannot be initialized to resources value outside
        minAge = resources.getInteger(R.integer.age_min)
        maxAge = resources.getInteger(R.integer.age_max)
    }

    override fun onResume() {
        super.onResume()
        isNewUser = intent.getBooleanExtra(getString(R.string.newUser_ExtraName), false)
        gender = intent.getStringExtra(User.VarName.gender.name)
        gender?.let{
            if(it.isNotEmpty()) {
                genderPosition = User.Gender.valueOf(it).ordinal
            }
        }

        description = intent.getStringExtra(User.VarName.description.name)
        description?.let{
            findViewById<TextView>(R.id.NUA_description).text = it
        }

        birthdate = intent.getLongExtra(
            User.VarName.birthdate.name,
            resources.getInteger(R.integer.default_birthdate).toLong()
        )
        if (birthdate != resources.getInteger(R.integer.default_birthdate).toLong()) {
            setBirthdateText(birthdate)
            findViewById<Button>(R.id.NUA_reset_birthdate).visibility = View.VISIBLE
        } else {
            findViewById<Button>(R.id.NUA_reset_birthdate).visibility = View.INVISIBLE
        }
        // access the items of the list
        val genders = User.Gender.values()
        // access the spinner
        val spinner = findViewById<Spinner>(R.id.gender_spinner)
        spinner?.let {
            val adapter = ArrayAdapter(
                this,
                R.layout.spinner_item,
                genders
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            it.adapter = adapter
            it.setSelection(genderPosition) // set default to last item of Gender: None
            it.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val chosen = parent.getItemAtPosition(position)
                    gender = if (chosen == User.Gender.None) {
                        ""
                    } else {
                        chosen.toString()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    fun selectBirthdate(view: View) {
        val calendar: Calendar = Calendar.getInstance() // current date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        calendar.add(Calendar.YEAR, -minAge)
        val year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(
                this,
                { _, mYear, mMonth, mDay -> setDate(mYear, mMonth, mDay) },
                year,
                month,
                day
            )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -maxAge)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.setIcon(R.drawable.logo)
        datePickerDialog.setTitle(R.string.new_user_birthdatePicker)
        datePickerDialog.show()
    }

    private fun setDate(year: Int, month: Int, day: Int) {
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, day)
        birthdate = cal.timeInMillis
        setBirthdateText(birthdate)

    }

    fun confirm(view: View) {
        val description: String = findViewById<EditText>(R.id.NUA_description).text.toString()
        startActivity(
            Intent(this, UserNewInfoActivity::class.java)
                .putExtra(User.VarName.description.name, description)
                .putExtra(User.VarName.gender.name, gender)
                .putExtra(User.VarName.birthdate.name, birthdate)
                .putExtra(getString(R.string.newUser_ExtraName), isNewUser)
        )
    }

    fun cancel(v: View) {
//        // new Alert Dialogue to ensure deletion
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        val positiveButtonClick = { _: DialogInterface, _: Int ->
        startActivity(
            Intent(this, UserNewInfoActivity::class.java)
                .putExtra(getString(R.string.newUser_ExtraName), isNewUser)
        )
//        }
//        val negativeButtonClick = { _: DialogInterface, _: Int -> }
//
//        builder.setTitle(getString(R.string.alert_dialogue_cancel_title))
//            .setMessage(getString(R.string.alert_dialogue_cancel_text))
//            .setCancelable(false)
//            .setPositiveButton(android.R.string.yes, positiveButtonClick)
//            .setNegativeButton(android.R.string.cancel, negativeButtonClick)
//            .create()
//            .show()
    }

    fun resetBirthdate(v: View) {
        birthdate = resources.getInteger(R.integer.default_birthdate).toLong()
        val text = findViewById<TextView>(R.id.NUA_selected_birthdate_text)
        text.text = resources.getString(R.string.no_birthdate_selected)
        findViewById<Button>(R.id.NUA_reset_birthdate).visibility = View.INVISIBLE
    }

    private fun setBirthdateText(birthdate: Long) {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = birthdate

        val text = findViewById<TextView>(R.id.NUA_selected_birthdate_text)
        text.text = "birthdate set to\n" +
                    "${cal.get(Calendar.DAY_OF_MONTH)}/" +
                    "${cal.get(Calendar.MONTH) + 1}/" +
                    "${cal.get(Calendar.YEAR)}"
        findViewById<Button>(R.id.NUA_reset_birthdate).visibility = View.VISIBLE
    }
}