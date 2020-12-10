package com.karine.retrieve.ui


import android.Manifest
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.karine.retrieve.R
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


open class BaseActivity : AppCompatActivity() {

    private lateinit var ab: ActionBar

    companion object {
        const val  RC_CAMERA_AND_STORAGE = 100
        val CAM_AND_READ_EXTERNAL_STORAGE = arrayOf<String>(
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
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

    @AfterPermissionGranted(RC_CAMERA_AND_STORAGE)
    open fun methodRequiresTwoPermission() {

        if (EasyPermissions.hasPermissions(this, *CAM_AND_READ_EXTERNAL_STORAGE)) {
            Log.d("Permissions", "Permissions granted")
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, "This application need permissions to access",
                RC_CAMERA_AND_STORAGE, *CAM_AND_READ_EXTERNAL_STORAGE
            )
        }
    }


}