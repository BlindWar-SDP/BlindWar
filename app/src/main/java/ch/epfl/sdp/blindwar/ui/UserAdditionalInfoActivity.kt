package ch.epfl.sdp.blindwar.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.user.Gender
import java.util.*

class UserAdditionalInfoActivity : AppCompatActivity() {

    private var gender: String? = null
    private var birthDate: Long = -1
    private var minAge = -1
    private var maxAge = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_additional_info)

        minAge = resources.getInteger(R.integer.age_min)
        maxAge = resources.getInteger(R.integer.age_max)

        // access the items of the list
        val genders = Gender.values()
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
            it.setSelection(adapter.count - 1) // set default to last item of Gender: Gender
            it.prompt = "Select a gender"
            it.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    if (parent.getItemAtPosition(position) != Gender.None) { // do nothing id default value
                        gender = parent.getItemAtPosition(position).toString()
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
                { _, mYear, mDay, mMonth -> setDate(mYear, mMonth + 1, mDay) },
                year,
                month,
                day
            )
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        calendar.add(Calendar.YEAR, -maxAge)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.setIcon(R.drawable.logo);
        datePickerDialog.setTitle(R.string.new_user_birthdatePicker)
        datePickerDialog.show()
    }

    private fun setDate(year: Int, month: Int, day: Int) {
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, day)
        birthDate = cal.timeInMillis
    }

    fun confirm(view: View) {
        val description: String = findViewById<EditText>(R.id.NUA_description).text.toString()
        val isNewUser = intent.getBooleanExtra("newUser", false)
        startActivity(Intent(this, UserNewInfoActivity::class.java)
            .putExtra("description", description)
            .putExtra("gender", gender)
            .putExtra("birthdate", birthDate)
            .putExtra("newUser", isNewUser)
        )
    }
}