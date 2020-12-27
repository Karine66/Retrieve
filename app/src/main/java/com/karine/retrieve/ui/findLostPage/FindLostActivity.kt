package com.karine.retrieve.ui.findLostPage


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.jama.carouselview.CarouselView
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityFindLostBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.BaseActivity
import com.karine.retrieve.ui.Carousel
import com.karine.retrieve.ui.UserObjectViewModel
import com.karine.retrieve.utils.Utils
import java.sql.Types.TIMESTAMP
import java.text.DateFormat
import java.util.*
import kotlin.properties.Delegates


open class FindLostActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {


    private var photoList: MutableList<Uri> = mutableListOf()
    private var pathListPhoto : MutableList<String> = mutableListOf()
    private var photo = pathListPhoto
    private var lostClick by Delegates.notNull<Int>()
    private var findClick by Delegates.notNull<Int>()

    private lateinit var userObject: UserObject
    private lateinit var findLostBinding: ActivityFindLostBinding
    private lateinit var userObjectViewModel: UserObjectViewModel
    private lateinit var ab: ActionBar
    private lateinit var builder: MaterialAlertDialogBuilder
    private lateinit var pathImageSavedInFirebase: Uri
    private lateinit var fileUri: Uri
    private lateinit var carouselView : CarouselView
    var firestoreDB = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().uid
    private val createdDate = Timestamp.now()


    companion object {
        const val RC_CAMERA = 100
        const val RC_GALLERY = 200
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findLostBinding = ActivityFindLostBinding.inflate(layoutInflater)
        val view = findLostBinding.root
        setContentView(view)

        configureToolbar()
        configureUpButton()
        dropDownAdapter()
        clickDate()
        configureViewModel()
        clickValidate()
        clickPhoto()


        //for retrieve click on speed dial

        findClick = intent.getIntExtra("findClick", 1)
        Log.d("findClick", "findClick$findClick")
        lostClick = intent.getIntExtra("lostClick", 0)
//        Log.d("lostClick", "lostClick$lostClick")

        //For toolbar
        ab = supportActionBar!!
        ab.title = getString(R.string.ajoutObjet)
        //For carousel
        findLostBinding.carousel.visibility = View.GONE

    }

    //for carousel
    private fun updateCarousel() {
        if (photoList.size >= 1) {
            findLostBinding.carousel.visibility = View.VISIBLE
        }
        carouselView = findLostBinding.carousel
        Carousel.carousel(carouselView, photoList)
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

            Snackbar.make(
                findLostBinding.root,
                getString(R.string.ajoutreussi),
                Snackbar.LENGTH_SHORT
            )
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(snackbar: Snackbar, event: Int) {
                        super.onDismissed(snackbar, event)
                        finish()
                    }
                })
                .show()
        })
    }

    //for click on photo button
    private fun clickPhoto() {
        findLostBinding.photoBtn.setOnClickListener(View.OnClickListener {
            limitedPhotos()
        })
    }

    private fun limitedPhotos(): Boolean {

        if (photoList.size < 3) {
            selectImage()
        return true
    }
    Snackbar.make(findLostBinding.root, getString(R.string.photosmax), Snackbar.LENGTH_SHORT).show()
    return false
    }

    //for save form in firebase
    private fun saveUserObject() {

        userObject = UserObject(

            user.toString(),
            createdDate,
            findLostBinding.etName.text.toString(),
            findLostBinding.etMail.text.toString(),
            findLostBinding.etPhone.text.toString().toIntOrNull(),
            findLostBinding.date.text.toString(),
            findLostBinding.etType.text.toString(),
            findLostBinding.etAddress.text.toString(),
            findLostBinding.etPostalCode.text.toString().toIntOrNull(),
            findLostBinding.etCity.text.toString(),
            findLostBinding.etDescription.text.toString(),
            photo
        )
        if (findClick==0) {
            this.userObjectViewModel.saveUserObjectFindToFirebase(userObject)
        }else if(lostClick==1) {
            this.userObjectViewModel.saveUserObjectLostToFirebase(userObject)
            Log.d("userObject", "UserObject$userObject")
        }
    }
    //for alert dialog photo
    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Add pictures")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {

                    ImagePicker.with(this).cameraOnly()
                        .start(RC_CAMERA)
                }
                options[item] == "Choose from Gallery" -> {
                    ImagePicker.with(this).galleryOnly()
                        .start(RC_GALLERY)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    //for photo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {

                    RC_CAMERA -> {

                        fileUri = data?.data!!
                        photoList.add(fileUri)
                        updateCarousel()
                        storeImageInFirestore()

                    }
                    RC_GALLERY -> {
                        fileUri = data?.data!!
                        photoList.add(fileUri)
                        updateCarousel()
                        storeImageInFirestore()

                    }
                    }
                }
            }
        }

    private fun storeImageInFirestore() {
        //For store photos in firebase
        val uuid = UUID.randomUUID().toString() // GENERATE UNIQUE STRING

        val mImageRef = FirebaseStorage.getInstance().getReference(uuid)
        val uploadTask = mImageRef.putFile(fileUri)

        val urlTask =
            uploadTask.continueWithTask { task: Task<UploadTask.TaskSnapshot?> ->
                if (!task.isSuccessful) {
                    Log.e(
                        "UploadPhoto",
                        "Error TASK_URI : " + task.exception
                    )
                    throw (task.exception)!!
                }
                mImageRef.downloadUrl
            }.addOnCompleteListener { task: Task<Uri?> ->
                if (task.isSuccessful) {

                      pathImageSavedInFirebase = task.result!!
                    pathListPhoto.add(pathImageSavedInFirebase.toString())
                    Log.d("pathImageFirebase", "pathiImageFirebase$pathImageSavedInFirebase")

                } else {
                    Log.e(
                        "UploadPhoto",
                        "Error ON_COMPLETE : " + task.exception
                    )
                }
            }
    }
}


















