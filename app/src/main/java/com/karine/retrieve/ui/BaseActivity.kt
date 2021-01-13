package com.karine.retrieve.ui


import android.Manifest
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.karine.retrieve.R
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


open class BaseActivity : AppCompatActivity() {

    private lateinit var ab: ActionBar

    companion object {
        const val RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION_CALL = 100
        val CAM_AND_READ_EXTERNAL_STORAGE_LOCATION_CALL = arrayOf<String>(
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE
        )
    }

    //for toolbar
    protected fun configureToolbar() {
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    //for up arrow
    protected open fun configureUpButton() {
        ab = supportActionBar!!
        ab.setDisplayHomeAsUpEnabled(true)
    }

    //for permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION_CALL)
    open fun methodRequiresFourPermission() {

        if (EasyPermissions.hasPermissions(this, *CAM_AND_READ_EXTERNAL_STORAGE_LOCATION_CALL)) {
            Log.d("Permissions", "Permissions granted")
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                "This application need permissions to access",
                RC_CAMERA_AND_STORAGE_COARSELOCATION_FINELOCATION_CALL,
                *CAM_AND_READ_EXTERNAL_STORAGE_LOCATION_CALL
            )
        }
    }
    //for firebase
    open fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
    open fun isCurrentUserLogged(): Boolean? {
        return getCurrentUser() != null
    }

    open fun onFailureListener(): OnFailureListener? {
        return OnFailureListener { e: Exception? ->
            Toast.makeText(applicationContext,getString(R.string.erreurInconnue),Toast.LENGTH_LONG).show()
        }
    }
}