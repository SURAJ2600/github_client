package com.suraj.githubclient.ui.ViewPullRequest

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.util.Log
import com.example.android.codelabs.paging.model.Repo
import com.example.android.codelabs.paging.model.RepoPullResult
import com.example.android.codelabs.paging.model.RepoSearchResult
import com.suraj.githubclient.model.PullRepoModel
import com.suraj.githubclient.repository.RepositoryServiceHandler
import com.suraj.githubclient.ui.SearchRepo.SearchRepoViewModel


/**
 * @author Suraj
 *
 *Viewmodel for [ViewRepoPullActvity]
 * The ViewModel works with the [RepositoryServiceHandler] to get the data.
 *
 */

enum class ViewRepoPullViewModeltate {
    LOADING, ERROR, NONE, SUCESS
}

class ViewRepoPullViewModel(private val repository: RepositoryServiceHandler) : ViewModel() {


    val state = MutableLiveData<ViewRepoPullViewModeltate>()


    private val ownername_livedate = MutableLiveData<String>()
    private val reponame_livedate = MutableLiveData<String>()


    var repoResult = MutableLiveData<RepoPullResult>()


    private val pullRequestReult: LiveData<RepoPullResult> = Transformations.map(ownername_livedate, {
        repository.getPullRequestFromRepo(it, reponame_livedate.value!!, { success ->

            state.value = ViewRepoPullViewModeltate.SUCESS

        }, { error ->
            state.value = ViewRepoPullViewModeltate.ERROR

        })

    })


    val repos: LiveData<PagedList<PullRepoModel>> = Transformations.switchMap(pullRequestReult,
        { it -> it.data })
    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult,
        { it -> it.networkErrors })


    fun setOwnerAndRepoName(ownername: String, reponame: String) {
        state.value = ViewRepoPullViewModeltate.LOADING
        reponame_livedate.postValue(reponame)
        ownername_livedate.postValue(ownername)

    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery1 = repoownername()
            val immutableQuery2 = reponame()

            if (immutableQuery1 != null&&immutableQuery2!=null) {
                repository.requestMorePullRequest(immutableQuery1,immutableQuery2)
            }
        }
    }

    fun repoownername(): String? = ownername_livedate.value
    fun reponame(): String? = reponame_livedate.value

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }
}