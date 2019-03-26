package com.suraj.githubclient.Utilities

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.view.LayoutInflater


object Util {


    /**
     * @author SURAJ
     * This is the common class for defining  constant values
     */


    val spinnerValues = arrayOf("Repo", "User")



    fun isConnected(context: Context): Boolean {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }




//    fun ShowProgressView(mCtx: Context): Dialog {
//        val factory = LayoutInflater.from(mCtx)
//
//        val DialogView = factory.inflate(R.layout.progressview, null)
//
//        val main_dialog = Dialog(mCtx)
//
//        main_dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        main_dialog.setCanceledOnTouchOutside(true)
//
//        main_dialog.setCancelable(true)
//
//
//        main_dialog.setContentView(DialogView)
//
//        return main_dialog
//    }

}
