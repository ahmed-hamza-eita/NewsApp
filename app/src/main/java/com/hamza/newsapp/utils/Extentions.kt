package com.hamza.newsapp.utils;

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.showToast(message: Any?) {
    Toast.makeText(requireContext(), "$message", Toast.LENGTH_LONG).show()
}

fun View.visibilityGone() {
    this.visibility = View.GONE
}

fun View.visibilityVisible() {
    this.visibility = View.VISIBLE
}