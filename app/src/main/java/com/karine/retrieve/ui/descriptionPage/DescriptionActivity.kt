package com.karine.retrieve.ui.descriptionPage


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.jama.carouselview.CarouselView
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityDescriptionBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.BaseActivity
import com.karine.retrieve.ui.Carousel
import com.karine.retrieve.ui.MapBoxViewModel
import com.karine.retrieve.ui.UserObjectViewModel
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import kotlin.properties.Delegates


class DescriptionActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var descriptionBinding: ActivityDescriptionBinding
    private lateinit var ab: ActionBar
    private lateinit var carouselView: CarouselView
    private lateinit var firstResultPoint: Point
    private lateinit var completeAddress : String
    private lateinit var mapBoxViewModel: MapBoxViewModel
    private lateinit var userObjectViewModel: UserObjectViewModel
    private lateinit var userObject: UserObject
    private var objectFind by Delegates.notNull<Boolean>()
    private var photoList: MutableList<Uri> = mutableListOf()
    private val userUid = FirebaseAuth.getInstance().uid
    private val REQUEST_CALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        descriptionBinding = ActivityDescriptionBinding.inflate(layoutInflater)
        val view = descriptionBinding.root
        setContentView(view)

        retrieveData()
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
    //retrieve data from click recyclerView
    private fun retrieveData() {
        userObject = intent.getParcelableExtra("userObject")
        objectFind = intent.getBooleanExtra("objectFind", true)
    }

    //for menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_description, menu)
        val deleteItem = menu!!.findItem(R.id.menu_delete)
        val user = userObject.uid
        deleteItem.isVisible = user == userUid
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_delete -> {
                   onClickDeleteObject()
                true
            }
            R.id.menu_partager -> {

                val subject = userObject.type
                val description = userObject.description
                val city = userObject.city
                val mail = userObject.email
                val message = "$description $city\n$mail\n\n Retrieve App"
                val mailIntent = Intent(Intent.ACTION_SEND)
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
                mailIntent.putExtra(Intent.EXTRA_TEXT,message)
                mailIntent.type = "message/rfc822"
                startActivity(Intent.createChooser(mailIntent, "Choose an email client"))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun onClickDeleteObject() {
        MaterialAlertDialogBuilder(this)
            .setMessage(resources.getString(R.string.suppannonce))
                .setNegativeButton(resources.getString(R.string.non)) { dialog, wich ->
                    dialog.dismiss()
                }
                .setPositiveButton(resources.getString(R.string.oui)) { dialog, wich ->

                    if(objectFind) {
                        userObjectViewModel.deleteObjectFind(userObject)
                    }else {
                        userObjectViewModel.deleteObjectLost(userObject)
                    }
                    Snackbar.make(descriptionBinding.root, getString(R.string.annoncesupp), Snackbar.LENGTH_SHORT)
                        .addCallback(object:Snackbar.Callback(){
                            override fun onDismissed(snackbar: Snackbar, event: Int) {
                                super.onDismissed(snackbar, event)
                                finish()
                            }
                        }).show()
                }
                .show()
    }


    //for update UI with data
    private fun updateUi() {

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

    //configure viewModel
    private fun configureViewModel() {
       mapBoxViewModel = ViewModelProvider(this).get(MapBoxViewModel::class.java)
       userObjectViewModel = ViewModelProvider(this).get(UserObjectViewModel::class.java)
    }

    //for click email button
    private fun clickEmail() {
        descriptionBinding.emailSend.setOnClickListener(View.OnClickListener {
            sendMail()
        })
    }


    private fun sendMail() {

        val mail = userObject.email
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
        val number = userObject.phone
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
        val number = userObject.phone
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
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 3000)

            mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                //for marker
                val symbolManager = SymbolManager(descriptionBinding.mapView, mapboxMap, style)
                symbolManager.iconAllowOverlap = true
                style.addImage(
                    "myMarker", BitmapFactory.decodeResource(
                        resources,
                        R.drawable.mapbox_marker_icon_default
                    )
                )
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
            val address: String? =userObject.address
            val postalCode: String = userObject.postalCode.toString()
            val city: String = userObject.city.toString()
            completeAddress = "$address $postalCode $city"
            Log.d("createString", "createString$completeAddress")
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
        descriptionBinding.mapView.onDestroy()
    }
}




