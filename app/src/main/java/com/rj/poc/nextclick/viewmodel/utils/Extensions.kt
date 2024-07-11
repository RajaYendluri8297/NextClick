package com.rj.poc.nextclick.viewmodel.utils

import android.view.View
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}
fun String.isEmailValid(): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
    return emailRegex.matches(this)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun Long.toMB(): Double {
    val megabytes = this / (1024.0 * 1024.0)
    return String.format("%.2f", megabytes).toDouble()
}
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


