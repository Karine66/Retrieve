package com.karine.retrieve.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


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