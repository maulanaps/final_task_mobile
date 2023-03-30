package com.example.submission05.utils.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.icu.text.DateTimePatternGenerator.PatternInfo.OK

class ErrorDialog {
    companion object {
        fun showError (activity: Activity, message: String) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Error")
            builder.setMessage(message)
            builder.setPositiveButton("OK") { _, _ ->
                activity.finish()
            }
            builder.show()
        }

    }
}