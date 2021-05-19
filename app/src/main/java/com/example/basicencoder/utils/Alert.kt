package com.example.basicencoder.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

fun alert(message: String, context: Context) {
    val dlgAlert = AlertDialog.Builder(context)
    dlgAlert.setMessage(message)
    dlgAlert.setTitle("Alert")
    dlgAlert.setPositiveButton("Ok",
        DialogInterface.OnClickListener { _, _ ->
            //dismiss the dialog
        })
    dlgAlert.setCancelable(true)
    dlgAlert.create().show()
}