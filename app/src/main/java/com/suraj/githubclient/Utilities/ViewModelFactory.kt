

package com.suraj.githubclient.Utilities

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import com.suraj.githubclient.repository.RepositoryServiceHandler
import com.suraj.githubclient.ui.SearchRepo.SearchRepoViewModel
import com.suraj.githubclient.ui.ViewPullRequest.ViewRepoPullViewModel


/**
 * Factory for ViewModels
 */
class ViewModelFactory(var context:Context,private val repository: RepositoryServiceHandler) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRepoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchRepoViewModel( context,repository) as T
        }
        else if(modelClass.isAssignableFrom(ViewRepoPullViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewRepoPullViewModel(context,repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}