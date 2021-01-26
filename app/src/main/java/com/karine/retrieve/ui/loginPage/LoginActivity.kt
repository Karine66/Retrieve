package com.karine.retrieve.ui.loginPage


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.karine.retrieve.R
import com.karine.retrieve.databinding.ActivityLoginBinding
import com.karine.retrieve.ui.mainPage.MainPageActivity


open class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    //Identifier for Sign-In Activity
    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //for ViewBinding
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        clickMailBtn()
        clickGoogleBtn()

    }

    /**
     * For click login with email
     */
    private fun clickMailBtn() {
        loginBinding.signInBtn.setOnClickListener {
            startSignInActivity()
        }
    }

    /**
     * For click login with Google
     */
    private fun clickGoogleBtn() {
        loginBinding.googleBtn.setOnClickListener {
            startSignInGoogleActivity()
        }
    }

    /**
     * Launch sign in by mail
     */
    private fun startSignInActivity() {

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    mutableListOf(AuthUI.IdpConfig.EmailBuilder().build())
                )
                .setIsSmartLockEnabled(false, true)
                .build(),
            RC_SIGN_IN
        )
    }

    /**
     * Launch sign in by Google
     */
    private fun startSignInGoogleActivity() {

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    mutableListOf(AuthUI.IdpConfig.GoogleBuilder().build())
                )
                .setIsSmartLockEnabled(false, true)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handleResponseAfterSignIn(requestCode, resultCode, data!!)
    }

    /**
     * Method that handles responses after SignIn Activity close
     */
    private fun handleResponseAfterSignIn(requestCode: Int, resultCode: Int, data: Intent) {
        val response = IdpResponse.fromResultIntent(data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                val loginIntent = Intent(this, MainPageActivity::class.java)
                startActivity(loginIntent)
                Snackbar.make(
                    loginBinding.root,
                    getString(R.string.authreussie),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else { // ERRORS
                if (response == null) {
                    Snackbar.make(
                        loginBinding.root,
                        getString(R.string.autherror),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(
                        loginBinding.root,
                        getString(R.string.errnointernet),
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (response.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    Snackbar.make(
                        loginBinding.root,
                        getString(R.string.errInconnue),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}











