package com.karine.retrieve.ui.descriptionPage


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityDescriptionBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.BaseActivity
import com.karine.retrieve.ui.Carousel
import com.karine.retrieve.ui.UserObjectViewModel
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*


class DescriptionActivity : BaseActivity(), OnMapReadyCallback {


    private lateinit var descriptionBinding: ActivityDescriptionBinding
    private lateinit var ab: ActionBar
    private lateinit var userObjectViewModel: UserObjectViewModel
    private val SOURCE_ID = "SOURCE_ID"
    private val ICON_ID = "ICON_ID"
    private val LAYER_ID = "LAYER_ID"
//    private val userObject = UserObject()
    private lateinit var userObject : UserObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        descriptionBinding = ActivityDescriptionBinding.inflate(layoutInflater)
        val view = descriptionBinding.root

       setContentView(view)

        configureToolbar()
        configureUpButton()
        configureViewModel()
//        updateUi(userObject)
        //For toolbar
        ab = supportActionBar!!
        ab.title = getString(R.string.description)


        descriptionBinding.mapView.onCreate(savedInstanceState)
        descriptionBinding.mapView.getMapAsync(this)


        }


    private fun configureViewModel() {
        userObjectViewModel = ViewModelProvider(this).get(UserObjectViewModel::class.java)

//        val intent = intent
//        val userObject: UserObject = intent.getParcelableExtra("userObject")
//        Log.d("userObjectDescription", "userObjectDescription$userObject")
//        userObjectViewModel.getSavedUserObjectFind().observe(this, this::updateUi)

    }

    private fun updateUi(userObject: UserObject) {

//        val userObject = UserObject {
            descriptionBinding.etType.setText(userObject.type)
            descriptionBinding.etDate.setText(userObject.date)
            descriptionBinding.etDescription.setText(userObject.description)
            descriptionBinding.etAddress.setText(userObject.address)
            descriptionBinding.etDate.setText(userObject.date)
            descriptionBinding.etCity.setText(userObject.city)
            descriptionBinding.contactPseudo.text = userObject.pseudo

        }

//    }

    override fun onMapReady(mapboxMap: MapboxMap) {

        mapboxMap.setStyle(
            Style.MAPBOX_STREETS

        )
        {
            // Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.
            val currentCameraPosition = mapboxMap.cameraPosition
            val currentZoom = currentCameraPosition.zoom
            val currentTilt = currentCameraPosition.tilt

            val position = CameraPosition.Builder()
                .target(LatLng(51.50550, -0.07520))
                .zoom(10.0)
                .tilt(20.0)
                .build()

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000)
            

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
        descriptionBinding.mapView.onDestroy()
    }
}


