package com.karine.retrieve.ui.descriptionPage


import android.R.id.message
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.core.net.toUri
import com.jama.carouselview.CarouselView
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityDescriptionBinding
import com.karine.retrieve.models.UserObject
import com.karine.retrieve.ui.BaseActivity
import com.karine.retrieve.ui.Carousel
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*


class DescriptionActivity : BaseActivity(), OnMapReadyCallback {


    private lateinit var descriptionBinding: ActivityDescriptionBinding
    private lateinit var ab: ActionBar
    private lateinit var carouselView : CarouselView
    private lateinit var markerView: MarkerView
   private var photoList : MutableList<Uri> = mutableListOf()

//    private lateinit var photoList : MutableList<String>
    private val SOURCE_ID = "SOURCE_ID"
    private val ICON_ID = "ICON_ID"
    private val LAYER_ID = "LAYER_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        descriptionBinding = ActivityDescriptionBinding.inflate(layoutInflater)
        val view = descriptionBinding.root

       setContentView(view)

        configureToolbar()
        configureUpButton()
        updateUi()
        clickEmail()
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

    //for click email button
    private fun clickEmail() {
        descriptionBinding.emailSend.setOnClickListener(View.OnClickListener {
         sendMail()
        })
    }

    private fun sendMail() {
        val userObject: UserObject? = intent.getParcelableExtra("userObject")

        val mail = userObject?.email
        Log.d("mail", "mail$mail")
        val subject = userObject?.type

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL,mail)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Choose an email client"))


    }


    override fun onMapReady(mapboxMap: MapboxMap) {
//       val markerViewManager = MarkerViewManager(descriptionBinding.mapView, mapboxMap)
        mapboxMap.setStyle(
            Style.MAPBOX_STREETS

        )
        {
//             Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.

            val currentCameraPosition = mapboxMap.cameraPosition
            val currentZoom = currentCameraPosition.zoom
            val currentTilt = currentCameraPosition.tilt

            val position = CameraPosition.Builder()
                .target(LatLng(51.50550, -0.07520))
                .zoom(10.0)
                .tilt(20.0)
                .build()

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 7000)


//            markerView = MarkerView(LatLng(51.50550, -0.07520),R.drawable.mapbox_marker_icon_default)
//            markerViewManager.addMarker(markerView)
//
        }

  }
//
//    private fun MarkerView(latLng: LatLng, mapboxMarkerIconDefault: Int): MarkerView {
//    return markerView
//    }


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


