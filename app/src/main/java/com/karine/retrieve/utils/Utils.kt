package com.karine.retrieve.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.karine.retrieve.utils.Utils.Companion.hideKeyboard
import java.sql.Types.TIMESTAMP
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun Fragment.hideKeyboard() {
            view?.let {
                activity?.hideKeyboard(it)
            }
        }


    }
}