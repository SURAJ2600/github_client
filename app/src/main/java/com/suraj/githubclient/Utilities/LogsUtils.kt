package com.suraj.githubclient.Utilities


import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.support.design.widget.Snackbar

import android.util.Log
import android.view.View
import android.widget.Toast


/*Created by suraj*/

/**
  A log util class for managing the app entire logs,toast,snack bar
 */

object LogsUtils {

    private var myObj: LogsUtils? = null

    /**
     * Create a static method to get instance.
     */

    fun makeLogD(TAG: String, message: String) {
        if(false) {
            Log.d(TAG, "" + message)
        }
    }

    fun makeLogE(TAG: String, message: String) {
        if(true) {
            Log.e(TAG, "" + message)
        }
    }




    fun snackBarAction(view: View, message: String?) {
        Snackbar.make(view, "$message", Snackbar.LENGTH_LONG)
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(snackbar: Snackbar?, event: Int) {
                        makeLogD("TAG", "snackbar dismissed")
                    }
                })
                .setAction("Ok") {
                    // do nothing
                    makeLogD("TAG", "undo action clicked")
                }

                .show()
    }

    fun snackBar(view: View,message: String){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show()
    }


    fun showToast(activity: Activity, message: String?) {
        Toast.makeText(activity, "$message" , Toast.LENGTH_SHORT).show()
    }


}
/**
 * Create private constructor
 */