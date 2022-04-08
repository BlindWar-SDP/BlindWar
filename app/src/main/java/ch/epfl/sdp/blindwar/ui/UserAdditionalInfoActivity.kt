package ch.epfl.sdp.blindwar.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.user.User
import java.util.*

class UserAdditionalInfoActivity : AppCompatActivity() {

    private var gender: String? = null
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
        birthdate = resources.getInteger(R.integer.default_birthdate).toLong()

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
            it.setSelection(adapter.count - 1) // set default to last item of Gender: None
            it.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val chosen = parent.getItemAtPosition(position)
                    gender = if ( chosen == User.Gender.None) {
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

    override fun onResume() {
        super.onResume()
        isNewUser = intent.getBooleanExtra(getString(R.string.newUser_ExtraName), false)
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
        Toast.makeText(
            this,
            "birthdate set to " +
                    "${cal.get(Calendar.DAY_OF_MONTH)}/" +
                    "${cal.get(Calendar.MONTH)+1}/" +
                    "${cal.get(Calendar.YEAR)}",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun confirm(view: View) {
        val description: String = findViewById<EditText>(R.id.NUA_description).text.toString()
        startActivity(
            Intent(this, UserNewInfoActivity::class.java)
                .putExtra(User.VarName.description.toString(), description)
                .putExtra(User.VarName.gender.toString(), gender)
                .putExtra(User.VarName.birthdate.toString(), birthdate)
                .putExtra(getString(R.string.newUser_ExtraName), isNewUser)
        )
    }

    fun cancel(v: View) {
//        // new Alert Dialogue to ensure deletion
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        val positiveButtonClick = { _: DialogInterface, _: Int ->
            startActivity(Intent(this, UserNewInfoActivity::class.java)
                .putExtra(getString(R.string.newUser_ExtraName), isNewUser))
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
        Toast.makeText(
            this,
            "birthdate reset", Toast.LENGTH_SHORT
        ).show()
    }
}