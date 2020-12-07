package com.karine.retrieve.ui.findLostPage


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityFindLostBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.UserObjectViewModel
import java.text.DateFormat
import java.util.*


class FindLostActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {


    private lateinit var userObject: UserObject
    private lateinit var findLostBinding: ActivityFindLostBinding
    private lateinit var userObjectViewModel: UserObjectViewModel
    var firestoreDB = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().uid



    private val KEY_PSEUDO = "pseudo"
    private val KEY_EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findLostBinding = ActivityFindLostBinding.inflate(layoutInflater)
        val view = findLostBinding.root
        setContentView(view)

        dropDownAdapter()
        clickDate()
        configureViewModel()
        clickValidate()

    }

    //for dropdown
    private fun factoryAdapter(resId: Int): ArrayAdapter<String?> {
        return ArrayAdapter(
            this,
            R.layout.dropdown_menu_popup_item,
            resources.getStringArray(resId)
        )
    }

    //for dropdown
    private fun dropDownAdapter() {
        findLostBinding.etType.setAdapter(factoryAdapter(R.array.Type))
    }

    //For date picker
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val c = Calendar.getInstance()
        c[Calendar.YEAR] = year
        c[Calendar.MONTH] = month
        c[Calendar.DAY_OF_MONTH] = dayOfMonth

        val currentDateString: String =
            DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)

        findLostBinding.date.setText(currentDateString)
    }

    //for click on date
    private fun clickDate() {

        findLostBinding.date.setOnClickListener(View.OnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        })
    }

    private fun configureViewModel() {
        userObjectViewModel = ViewModelProvider(this).get(UserObjectViewModel::class.java)

    }

    //for click validate button
    private fun clickValidate() {

        findLostBinding.validateFabBtn.setOnClickListener(View.OnClickListener {

            saveUserObject()
            Snackbar.make(findLostBinding.root, "Ajout réussi", Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun saveUserObject() {

        userObject = UserObject(

            user.toString(),
            findLostBinding.etName.text.toString(),
            findLostBinding.etMail.text.toString(),
            findLostBinding.etPhone.text.toString().toIntOrNull(),
            findLostBinding.date.text.toString().toLongOrNull(),
            findLostBinding.etType.text.toString(),
            findLostBinding.etAddress.text.toString(),
            findLostBinding.etPostalCode.text.toString().toIntOrNull(),
            findLostBinding.etCity.text.toString(),
            findLostBinding.etDescription.text.toString()
        )
        userObjectViewModel.saveUserObjectToFirebase(userObject)
        Log.d("userObject", "UserObject$userObject")

            val userId = FirebaseAuth.getInstance().currentUser

            firestoreDB.collection("users").document(userId!!.email.toString())
                .set(userObject)
                .addOnSuccessListener {

                    Log.d("addObject", "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e -> Log.w("failureAddObject", "Error writing document", e) }
        }
        }











