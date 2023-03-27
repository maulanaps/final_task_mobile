package com.example.submission05.dialog

import android.app.Activity
import android.app.AlertDialog
import com.example.submission03.R

class LoadingDialog {
    private lateinit var dialogBox: AlertDialog
    fun startLoading(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setView(activity.layoutInflater.inflate(R.layout.loading_item, null))
        builder.setCancelable(false)
        dialogBox = builder.create()
        dialogBox.show()
    }

    fun endLoading() {
        dialogBox.dismiss()
    }
}