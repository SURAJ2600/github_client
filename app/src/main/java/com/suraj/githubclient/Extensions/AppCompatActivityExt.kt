package com.suraj.githubclient.Extensions

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import com.suraj.githubclient.Utilities.Injection
import com.suraj.githubclient.Utilities.ViewModelFactory
import com.suraj.githubclient.ui.SearchRepo.SearchRepoViewModel


/**
 * Created by suraj s on 3/2/18.
 */




/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */


fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
  .get(viewModelClass)


