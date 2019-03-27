package com.suraj.githubclient.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.suraj.githubclient.R
import com.suraj.githubclient.ui.SearchRepo.SearchGitRepoActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        /*
        * Remove status bar from top of the screen
        *
        * */


        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );


        getHandler();
    }

    private fun getHandler() {

        Handler().postDelayed({


            val mainIntent = Intent(this, SearchGitRepoActivity::class.java)
            startActivity(mainIntent)
            finish()


        }, 3000)


    }
}
