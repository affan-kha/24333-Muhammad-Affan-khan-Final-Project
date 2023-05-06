package com.example.affanstore

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Utils {

    companion object{
        var alert: AlertDialog? = null

        fun startLoading(context: Context) {
            val builder = MaterialAlertDialogBuilder(context)
            val layout = LayoutInflater.from(context).inflate(R.layout.custom_loading, null)
            builder.setView(layout)
            builder.setCancelable(false)
            alert = builder.create()
            alert!!.show()
        }

        fun dismiss() {
            if (alert != null) {
                alert!!.dismiss()
            }
        }
    }

}