package com.karine.retrieve.ui.descriptionPage


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.jama.carouselview.CarouselView
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityDescriptionBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.BaseActivity
import com.karine.retrieve.ui.Carousel
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class DescriptionActivity : BaseActivity(), OnMapReadyCallback {


    private lateinit var descriptionBinding: ActivityDescriptionBinding
    private lateinit var ab: ActionBar
    private lateinit var carouselView: CarouselView
    private lateinit var urlPhoto: String
    private lateinit var uriImageSelected: Uri
    private lateinit var firstResultPoint: Point
    private lateinit var completeAddress : String
    //   private lateinit var downloadUri :Uri
    private var photoList: MutableList<Uri> = mutableListOf()

    private val REQUEST_CALL = 1
    var storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        descriptionBinding = ActivityDescriptionBinding.inflate(layoutInflater)
        val view = descriptionBinding.root

        setContentView(view)

        configureToolbar()
        configureUpButton()
        updateUi()
        clickEmail()
        clickCall()
        createStringForAddress()
        geocodeSearch()

//        updateCarousel()
        //For toolbar
        ab = supportActionBar!!
        ab.title = getString(R.string.description)


        descriptionBinding.mapView.onCreate(savedInstanceState)
        descriptionBinding.mapView.getMapAsync(this)


    }

    private fun updateUi() {

        val userObject: UserObject? = intent.getParcelableExtra("userObject")
        Log.d("userObjectDescription", "userObjectDescription$userObject")

        if (userObject != null) {
            descriptionBinding.etType.setText(userObject.type)
            descriptionBinding.etType.isEnabled = false
            descriptionBinding.etDate.setText(userObject.date)
            descriptionBinding.etDate.isEnabled = false
            descriptionBinding.etDescription.setText(userObject.description)
            descriptionBinding.etDescription.isEnabled = false
            descriptionBinding.etAddress.setText(userObject.address)
            descriptionBinding.etAddress.isEnabled = false
            descriptionBinding.etPostalCode.setText((userObject.postalCode!!).toString())
            descriptionBinding.etPostalCode.isEnabled = false
            descriptionBinding.etCity.setText(userObject.city)
            descriptionBinding.etCity.isEnabled = false
            descriptionBinding.contactPseudo.text = userObject.pseudo
            descriptionBinding.contactPseudo.isEnabled = false

//            storageRef.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
//                urlPhoto = uri.toString()
//
//            }).addOnFailureListener(OnFailureListener { })

//            uploadPhotoInFirebase()
            photoList.clear()
            if (userObject.photo.isNotEmpty() && userObject.photo.size > 0) {
                for (photoStr: String in userObject.photo) {
                    photoList.add(photoStr.toUri())
                    carouselView = descriptionBinding.carousel
                    Carousel.carousel(carouselView, photoList)
                    Log.d("photolistDescription", "photolistDescription$photoList")
                }
            }
        }
    }
//    private fun uploadPhotoInFirebase() {
//        val uuid = UUID.randomUUID().toString() // GENERATE UNIQUE STRING
//
//        //  Upload
//        val mImageRef = FirebaseStorage.getInstance().getReferenceFromUrl("")
//
//        val uploadTask = mImageRef.putFile(uriImageSelected)
//        val urlTask = uploadTask.continueWithTask { task: Task<UploadTask.TaskSnapshot?> ->
//            if (!task.isSuccessful) {
//                Log.e("UploadPhoto", "Error TASK_URI : " + task.exception)
//                throw (task.exception!!)
//            }
//            mImageRef.downloadUrl
//        }.addOnCompleteListener { task: Task<Uri?> ->
//            if (task.isSuccessful) {
//                downloadUri = task.result!!
//
//                Log.d("UploadPhoto", "UploadPhoto$downloadUri")
//
//            } else {
//                Log.e("UploadPhoto", "Error ON_COMPLETE : " + task.exception)
//            }
//        }
//    }


    //for click email button
    private fun clickEmail() {
        descriptionBinding.emailSend.setOnClickListener(View.OnClickListener {
            sendMail()
        })
    }

    private fun sendMail() {
        val userObject: UserObject? = intent.getParcelableExtra("userObject")

        val mail = userObject?.email
        val mails: Array<String> = mail!!.split(",").toTypedArray()
        Log.d("mail", "mail$mail")
        val subject = userObject.type

        val mailIntent = Intent(Intent.ACTION_SEND)
        mailIntent.putExtra(Intent.EXTRA_EMAIL, mails)
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mailIntent.type = "message/rfc822"
        startActivity(Intent.createChooser(mailIntent, "Choose an email client"))

    }

    //for call directly
    private fun clickCall() {
        descriptionBinding.callSend.setOnClickListener(View.OnClickListener {
            makeCall()
        })
    }


    private fun makeCall() {
        val userObject: UserObject? = intent.getParcelableExtra("userObject")
        val number = userObject?.phone
        if (number != null) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CALL
                )
            } else {
                val dial = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onMapReady(mapboxMap: MapboxMap) {
        val markerViewManager = MarkerViewManager(descriptionBinding.mapView, mapboxMap)
        mapboxMap.setStyle(
            Style.MAPBOX_STREETS

        )
        {
//             Map is set up and the style has loaded. Now you can add additional data or make other map adjustment


            val position = CameraPosition.Builder()
                .target(LatLng(firstResultPoint.latitude(), firstResultPoint.longitude()))
                .zoom(15.0)
                .tilt(20.0)
                .build()


            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000)
//                    val marker = MarkerView(LatLng(firstResultPoint.latitude(), firstResultPoint.longitude()),R.drawable.mapbox_marker_icon_default)
//            markerViewManager.addMarker(marker)
            mapboxMap.addMarker(
                MarkerOptions()
                    .position(LatLng(firstResultPoint.latitude(), firstResultPoint.longitude()))
            )

        }

    }
    private fun createStringForAddress() {
        val userObject: UserObject? = intent.getParcelableExtra("userObject")

        if (userObject != null) {
            val address: String? =userObject.address
            val postalCode: String = userObject.postalCode.toString()
            val city: String = userObject.city.toString()
            completeAddress = "$address $postalCode $city"
            Log.d("createString", "createString$completeAddress")
        }
    }

    private fun geocodeSearch() {



        val mapBoxGeocoding = MapboxGeocoding.builder()
            .accessToken(getString(R.string.access_token))
            .query(completeAddress)
            .build()

        mapBoxGeocoding.enqueueCall(object : Callback<GeocodingResponse> {
            override fun onResponse(
                call: Call<GeocodingResponse>,
                response: Response<GeocodingResponse>
            ) {

                val results = response.body()!!.features()

                if (results.size > 0) {

                    // Log the first results Point.
                    firstResultPoint = results[0].center()!!
                    Log.d("onResponse", "onResponse: $firstResultPoint")


                } else {

                    // No result for your request were found.
                    Log.d("OnNoResponse", "onResponse: No result found")

                }
            }

            override fun onFailure(call: Call<GeocodingResponse>, throwable: Throwable) {
                throwable.printStackTrace()
            }
        })

    }



    override fun onStart() {
        super.onStart()
        descriptionBinding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        descriptionBinding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        descriptionBinding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        descriptionBinding.mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        descriptionBinding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        descriptionBinding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        descriptionBinding.mapView.onDestroy();

    }
}




