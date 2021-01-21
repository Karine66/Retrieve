package com.karine.retrieve.ui.findLostPage


import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
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
import com.karine.retrieve.App
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityFindLostBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.BaseActivity
import com.karine.retrieve.ui.Carousel
import com.karine.retrieve.ui.SaveUserObjectViewModel
import com.karine.retrieve.ui.UserObjectViewModel
import org.koin.android.viewmodel.ext.android.viewModel
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
//   private lateinit var saveUserObjectViewModel: SaveUserObjectViewModel
    private lateinit var ab: ActionBar
    private lateinit var builder: MaterialAlertDialogBuilder
    private lateinit var pathImageSavedInFirebase: Uri
    private lateinit var fileUri: Uri
    private lateinit var carouselView : CarouselView
    private lateinit var pseudo : String
    private lateinit var email : String
    private lateinit var typeObject: String
    private lateinit var address : String
    private lateinit var postalCode : String
    private lateinit var city : String
    private lateinit var description : String
    private lateinit var docId : String

    private val saveUserObjectViewModel:SaveUserObjectViewModel by viewModel()
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
        initTexWatcher()
        //for retrieve click on speed dial
        findClick = intent.getIntExtra("findClick", 1)
        lostClick = intent.getIntExtra("lostClick", 0)
        //For toolbar
        ab = supportActionBar!!
        ab.title = getString(R.string.ajoutObjet)
        //For carousel
        findLostBinding.carousel.visibility = View.GONE
        //for textWatcher
        findLostBinding.etName.addTextChangedListener(textWatcher)
        findLostBinding.etMail.addTextChangedListener(textWatcher)
        findLostBinding.etType.addTextChangedListener(textWatcher)
        findLostBinding.etAddress.addTextChangedListener(textWatcher)
        findLostBinding.etPostalCode.addTextChangedListener(textWatcher)
        findLostBinding.etCity.addTextChangedListener(textWatcher)
        findLostBinding.etDescription.addTextChangedListener(textWatcher)

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
            DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.time)

        findLostBinding.date.setText(currentDateString)
    }

    //for click on date
    private fun clickDate() {

        findLostBinding.date.setOnClickListener(View.OnClickListener {
            val datePicker: DialogFragment = DatePickerFragment()
            datePicker.show(supportFragmentManager, "date picker")
        })
    }
    //configure viewModel
    private fun configureViewModel() {
        userObjectViewModel = ViewModelProvider(this).get(UserObjectViewModel::class.java)
//       saveUserObjectViewModel = ViewModelProvider(this).get( SaveUserObjectViewModel::class.java)
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
        docId = ""

        userObject = UserObject(
            docId,
            user.toString(),
            createdDate,
            findLostBinding.etName.text.toString(),
            findLostBinding.etMail.text.toString(),
            findLostBinding.etPhone.text.toString(),
            findLostBinding.date.text.toString(),
            findLostBinding.etType.text.toString(),
            findLostBinding.etAddress.text.toString(),
            findLostBinding.etPostalCode.text.toString().toIntOrNull(),
            findLostBinding.etCity.text.toString(),
            findLostBinding.etDescription.text.toString(),
            photo
        )
        if (findClick==0) {
//          this.userObjectViewModel.saveUserObjectFindToFirebase(userObject)
            this.saveUserObjectViewModel.saveUserObjectFindToFirestore(userObject)

        }else if(lostClick==1) {
//            this.userObjectViewModel.saveUserObjectLostToFirebase(userObject)
           this.saveUserObjectViewModel.saveUserObjectLostToFirestore(userObject)
            Log.d("userObject", "UserObject$userObject")
        }
    }
    //for alert dialog photo
    private fun selectImage() {
        val options = arrayOf<CharSequence>(
            getString(R.string.prendrePhoto), getString(R.string.gallery), getString(
                R.string.annuler
            )
        )
        builder = MaterialAlertDialogBuilder(this)
        builder.setTitle(getString(R.string.ajouter))
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == getString(R.string.prendrePhoto) -> {

                    ImagePicker.with(this).cameraOnly()
                        .start(RC_CAMERA)
                }
                options[item] == getString(R.string.gallery) -> {
                    ImagePicker.with(this).galleryOnly()
                        .start(RC_GALLERY)
                }
                options[item] == getString(R.string.annuler) -> {
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

    private fun initTexWatcher() {
        pseudo = findLostBinding.inputName.editText!!.text.toString().trim()
        email = findLostBinding.inputMail.editText!!.text.toString().trim()
        typeObject = findLostBinding.inputType.editText!!.text.toString().trim()
        address = findLostBinding.inputAddress.editText!!.text.toString().trim()
        postalCode = findLostBinding.inputPostalCode.editText!!.text.toString().trim()
        city = findLostBinding.inputCity.editText!!.text.toString().trim()
        description = findLostBinding.inputDescription.editText!!.text.toString().trim()
    }
//    For invalidate submit button if no all fields required are fill up
    private var textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            initTexWatcher()
            findLostBinding.validateFabBtn.isEnabled = (pseudo.isNotEmpty() && email.isNotEmpty() && typeObject.isNotEmpty()
                    &&  address.isNotEmpty() && postalCode.isNotEmpty() && city.isNotEmpty() && description.isNotEmpty())
        }
        override fun afterTextChanged(s: Editable) {
            if(pseudo.isEmpty()){
                findLostBinding.etName.error = getString(R.string.requis)
            }
            if(email.isEmpty()){
                findLostBinding.etMail.error = getString(R.string.requis)
            }
            if(typeObject.isEmpty()){
                findLostBinding.etType.error = getString(R.string.requis)
            }
            if(address.isEmpty()){
                findLostBinding.etAddress.error = getString(R.string.requis)
            }
            if(postalCode.isEmpty()){
                findLostBinding.etPostalCode.error = getString(R.string.requis)
            }
            if(city.isEmpty()) {
                findLostBinding.etCity.error = getString(R.string.requis)
            }
            if(description.isEmpty()){
                findLostBinding.etDescription.error = getString(R.string.requis)
            }
        }
    }
}


















