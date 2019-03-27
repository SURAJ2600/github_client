package com.suraj.githubclient.ui.ViewPullRequest

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.android.codelabs.paging.model.Repo
import com.example.android.codelabs.paging.model.RepoSearchResult
import com.suraj.githubclient.repository.RepositoryServiceHandler


/**
 * @author Suraj
 *
 *Viewmodel for [SearchGitRepoActivity]
 * The ViewModel works with the [RepositoryServiceHandler] to get the data.
 *
 */

enum class ViewRepoPullViewModeltate{
    LOADING,ERROR,NONE,SUCESS
}

class ViewRepoPullViewModel(private val repository: RepositoryServiceHandler) : ViewModel() {



      val state = MutableLiveData<ViewRepoPullViewModeltate>()



    fun getPullRequestFromRepo(githubOwnerName:String,githubRepoName:String) {
        state.value=ViewRepoPullViewModeltate.LOADING
        Log.e("rrponame",">>>>"+githubOwnerName+githubRepoName)
        repository.getPullRequestFromRepo(githubOwnerName, githubRepoName)
        state.value=ViewRepoPullViewModeltate.SUCESS



    }







}