package com.karine.retrieve.ui.findLostPage


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityFindLostBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.UserObjectViewModel
import java.lang.reflect.Array.set
import java.text.DateFormat
import java.util.*


class FindLostActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener{


    private lateinit  var userObject: UserObject
    private lateinit var findLostBinding: ActivityFindLostBinding
    var firestoreDB = FirebaseFirestore.getInstance()
//    private val objectRef: DocumentReference = firestoreDB.document("save_UserObject")

    private val KEY_PSEUDO = "pseudo"
    private val KEY_EMAIL = "email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findLostBinding = ActivityFindLostBinding.inflate(layoutInflater)
        val view = findLostBinding.root
        setContentView(view)

        dropDownAdapter()
        clickDate()
        clickValidate()
        configureViewModel()


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

        val currentDateString: String = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.time)

         findLostBinding.date.setText(currentDateString)
    }
    //for click on date
    private fun clickDate() {

        findLostBinding.date.setOnClickListener(View.OnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        })
    }

    fun configureViewModel() {
        val userObjectViewModel = ViewModelProvider(this).get(UserObjectViewModel::class.java)
//        userObjectViewModel.saveUserObjectToFirebase(userObject)
    }

    //for click validate button
    fun clickValidate() {
        saveUserObject()

    }


    private fun saveUserObject() {


        val pseudo: String = findLostBinding.etName.text.toString()
        val email: String = findLostBinding.etMail.text.toString()

        var saveObject: MutableMap<String, Any> = HashMap()

        saveObject[KEY_PSEUDO] = pseudo
        saveObject[KEY_EMAIL] = email

        firestoreDB.collection("users").document("save_UserObject")
            .set(saveObject)
            .addOnSuccessListener {

                Log.d("addObject", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("failureAddObject", "Error writing document", e) }
    }
        }








