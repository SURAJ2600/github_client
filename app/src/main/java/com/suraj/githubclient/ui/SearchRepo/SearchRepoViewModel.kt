package com.suraj.githubclient.ui.SearchRepo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.content.Context
import com.example.android.codelabs.paging.model.Repo
import com.example.android.codelabs.paging.model.RepoSearchResult
import com.suraj.githubclient.Utilities.Util
import com.suraj.githubclient.repository.RepositoryServiceHandler


/**
 * @author Suraj
 *
 *Viewmodel for [SearchGitRepoActivity]
 * The ViewModel works with the [RepositoryServiceHandler] to get the data.
 *
 */

enum class SearchRepoViewModelState {
    LOADING, ERROR, NONE, SUCESS, INTERNET
}

class SearchRepoViewModel(var context: Context, private val repository: RepositoryServiceHandler) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private var spinner_item = MutableLiveData<Int>()
    private val queryLiveData = MutableLiveData<String>()
    val state = MutableLiveData<SearchRepoViewModelState>()


    init {
        state.value = SearchRepoViewModelState.NONE
    }

    private val repoResult: LiveData<RepoSearchResult> = Transformations.map(queryLiveData, {
        repository.search(it, spinner_item.value!!)

    })


    val repos: LiveData<PagedList<Repo>> = Transformations.switchMap(repoResult,
        { it -> it.data })
    val networkErrors: LiveData<String> = Transformations.switchMap(repoResult,
        { it -> it.networkErrors })

    /**
     * Search a repository based on a query string.
     */
    fun searchRepo(queryString: String) {


        state.value = SearchRepoViewModelState.LOADING


        queryLiveData.postValue(queryString)

        state.value = SearchRepoViewModelState.NONE
    }

    fun setSpinnerValue(type: Int) {

        spinner_item.postValue(type)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = lastQueryValue()
            if (immutableQuery != null) {
                repository.requestMore(immutableQuery, spinner_item.value!!)
            }
        }
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value
}