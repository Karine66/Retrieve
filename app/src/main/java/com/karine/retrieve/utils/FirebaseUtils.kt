package com.karine.retrieve.utils

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.karine.retrieve.R
import com.karine.retrieve.utils.FirebaseUtils.Companion.getCurrentUser


class FirebaseUtils {

    private val context: Context? = null

    companion object {
        fun getCurrentUser(): FirebaseUser? {
            return FirebaseAuth.getInstance().currentUser
        }

    fun isCurrentUserLogged(): Boolean? {
        return getCurrentUser() != null
    }
}
//    /**
//     * Error Handler
//     * @return
//     */
//    fun onFailureListener(): OnFailureListener? {
//        return OnFailureListener { e: Exception? ->
//            Snackbar.make(
//                ,
//                R.string.unknown_error,
//                Snackbar.LENGTH_SHORT
//            ).show()
//        }
//    }
}