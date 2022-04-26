package ch.epfl.sdp.blindwar.login

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.profile.model.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import java.util.*

/**
 * Activity that let the user enter its secondary information when registering for the app
 * TODO: add KDOC / fix CodeClimate issues / fix Cirrus warning when possible
 * @constructor creates a UserAdditionalInfoActivity
 */
class UserAdditionalInfoActivity : AppCompatActivity() {

    private var minAge = -1
    private var maxAge = -1
    private var isNewUser = false

    private var user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_additional_info)

        // cannot be initialized to resources value outside
        minAge = resources.getInteger(R.integer.age_min)
        maxAge = resources.getInteger(R.integer.age_max)

        intent.extras?.let { bundle ->
            val serializable = bundle.getString(User.VarName.user.name)
            serializable?.let { userStr ->
                user = Json.decodeFromString(userStr)
            }
            isNewUser = bundle.getBoolean(resources.getString(R.string.newUser_ExtraName), false)
        }
        // set view
        findViewById<TextView>(R.id.NUA_description).text = user.description
        if (user.birthdate != resources.getInteger(R.integer.default_birthdate).toLong()) {
            setBirthdateText(user.birthdate)
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
            if (user.gender.isNotEmpty()) {
                // because default value for gender is "", which is not in the Enum Class
                it.setSelection(User.Gender.valueOf(user.gender).ordinal)
            } else {
                it.setSelection(adapter.count - 1)
            }
            it.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val chosen = parent.getItemAtPosition(position)
                    user.gender = if (chosen == User.Gender.None) {
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
        user.birthdate = cal.timeInMillis
        setBirthdateText(user.birthdate)

    }

    fun confirm(view: View) {
        user.description = findViewById<EditText>(R.id.NUA_description).text.toString()
        intent.extras?.let {
            it.putSerializable(
                User.VarName.user.name,
                Json.encodeToString(User.serializer(), user)
            )
            startActivity(
                Intent(this, UserNewInfoActivity::class.java)
                    .putExtras(it)
            )
        }
    }

    fun cancel(v: View) {
        intent.extras?.let {
            startActivity(
                Intent(this, UserNewInfoActivity::class.java)
                    .putExtras(it)
            )
        }
    }

    fun resetBirthdate(v: View) {
        user.birthdate = resources.getInteger(R.integer.default_birthdate).toLong()
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