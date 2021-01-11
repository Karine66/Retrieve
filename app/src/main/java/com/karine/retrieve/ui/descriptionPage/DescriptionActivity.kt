package com.karine.retrieve.ui.descriptionPage


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.jama.carouselview.CarouselView
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityDescriptionBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.BaseActivity
import com.karine.retrieve.ui.Carousel
import com.karine.retrieve.ui.MapBoxViewModel
import com.karine.retrieve.ui.UserObjectViewModel
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DescriptionActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var descriptionBinding: ActivityDescriptionBinding
    private lateinit var ab: ActionBar
    private lateinit var carouselView: CarouselView
    private lateinit var firstResultPoint: Point
    private lateinit var completeAddress : String
    private lateinit var mapBoxViewModel: MapBoxViewModel
    private var photoList: MutableList<Uri> = mutableListOf()

    private val REQUEST_CALL = 1

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
        btnCallVisibility()
        configureViewModel()
        //For toolbar
        ab = supportActionBar!!
        ab.title = getString(R.string.description)
        //for mapbox
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

            photoList.clear()
           if(userObject.photo.isNotEmpty()) {
               carouselView = descriptionBinding.carousel
               Carousel.carouselFromUrl(carouselView, userObject.photo)
           }
        }
    }
    //configure viewModel
    private fun configureViewModel() {
       mapBoxViewModel = ViewModelProvider(this).get(MapBoxViewModel::class.java)
    }

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
        }
    }
    //for visibility call btn
    private fun btnCallVisibility() {
        val userObject: UserObject? = intent.getParcelableExtra("userObject")
        val number = userObject?.phone
        if (number.isNullOrEmpty()) {
            descriptionBinding.callSend.visibility = View.INVISIBLE
        } else {
            descriptionBinding.callSend.visibility = View.VISIBLE
        }
    }
    //for map marker
    override fun onMapReady(mapboxMap: MapboxMap) {

        mapBoxViewModel.geocodingPoint(completeAddress).observe(this, Observer {
            val position = CameraPosition.Builder()
                .target(LatLng(it.latitude(), it.longitude()))
                .zoom(15.0)
                .tilt(20.0)
                .build()
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000)

        mapboxMap.setStyle(Style.MAPBOX_STREETS) {style->
            //for marker
            val symbolManager = SymbolManager(descriptionBinding.mapView, mapboxMap, style)
            symbolManager.iconAllowOverlap = true
            style.addImage("myMarker", BitmapFactory.decodeResource(resources,R.drawable.mapbox_marker_icon_default))
            symbolManager.create(
                SymbolOptions()
                    .withLatLng(LatLng(it.latitude(), it.longitude()))
                    .withIconImage("myMarker")
            )
        }
        })
    }
    //create string for geocoding
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




