package com.eohi.hx.utils

import android.app.Activity
import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eohi.hx.App
import com.eohi.hx.R
import com.google.android.material.snackbar.Snackbar

object Extensions {

    //View display
    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide() {
        this.visibility = View.INVISIBLE
    }

    fun String.trimStr() :String{
        return this.trim { it <= ' ' }
    }


    fun View.gone() {
        this.visibility = View.GONE
    }

    fun String?.valid(): Boolean =
        this != null && !(this.equals("null", true)) && this.trim().isNotEmpty()

    //Email Validation
    fun String.isValidEmail(): Boolean =
        this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    // Activity related
    inline fun <reified T : Any> Activity.getValue(
        label: String, defaultValue: T? = null
    ) = lazy {
        val value = intent?.extras?.get(label)
        if (value is T) value else defaultValue
    }

    inline fun <reified T : Any> Activity.getValueNonNull(
        label: String, defaultValue: T? = null
    ) = lazy {
        val value = intent?.extras?.get(label)
        requireNotNull((if (value is T) value else defaultValue)) { label }
    }

    // Fragment related
    inline fun <reified T : Any> Fragment.getValue(label: String, defaultValue: T? = null) = lazy {
        val value = arguments?.get(label)
        if (value is T) value else defaultValue
    }

    inline fun <reified T : Any> Fragment.getValueNonNull(label: String, defaultValue: T? = null) =
        lazy {
            val value = arguments?.get(label)
            requireNotNull(if (value is T) value else defaultValue) { label }
        }

    fun Int.asColor() = ContextCompat.getColor(App.instance, this)
    fun Int.asDrawable() = ContextCompat.getDrawable(App.instance, this)

    // Show alert dialog
    fun Context.showAlertDialog(
        positiveButtonLabel: String = getString(R.string.okay),
        title: String = getString(R.string.app_name), message: String,
        actionOnPositiveButton: () -> Unit
    ) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(positiveButtonLabel) { dialog, _ ->
                dialog.cancel()
                actionOnPositiveButton()
            }
        val alert = builder.create()
        alert.show()
    }

    // Toast extensions
    fun Context.showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Context.showLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    // Snackbar Extensions
    fun View.showShortSnackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
    }

    fun View.showLongSnackbar(message: String) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }

    fun View.snackBarWithAction(
        message: String, actionLabel: String,
        block: () -> Unit
    ) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG)
            .setAction(actionLabel) {
                block()
            }
    }


}