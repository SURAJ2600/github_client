package com.suraj.githubclient.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.example.android.codelabs.paging.model.Repo
import com.example.android.codelabs.paging.model.RepoPullResult
import com.example.android.codelabs.paging.model.RepoSearchResult
import com.suraj.githubclient.Utilities.LogsUtils
import com.suraj.githubclient.api.ApiService
import com.suraj.githubclient.api.searchPullFromRepo
import com.suraj.githubclient.api.searchRepos
import com.suraj.githubclient.db.GithubLocalCache
import io.reactivex.disposables.CompositeDisposable


/*Created by suraj on 27 march*/

/**
 *
 * @param service
 * @param cache
 * This class is used to handle all the  local and remote data source
 */
class RepositoryServiceHandler(
    private val service: ApiService,
    private val cache: GithubLocalCache
) {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage_SearchRepo = 1

    // LiveData of network errors.
    private val networkErrors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress_SearchRepo = false


    //CompositeDisposable for disposing the rxjava call to avoid memory leak
    //Now we defin two CompositeDisposable for each viewmodel and its cll we cn also make this as single by using the network request in common screen


    private val disposables = CompositeDisposable()



    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage_ViewPull = 1
    // avoid triggering multiple requests in the same time
    private var isRequestInProgress_ViewPull = false

    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String, type: Int): RepoSearchResult {
        Log.d("GithubRepository", "New query: $query")
        lastRequestedPage_SearchRepo = 1
        requestAndSaveData(query, type)

        val dataSourceFactory = cache.reposByName(query)

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).build()

        return RepoSearchResult(data, networkErrors)
    }


    //search repo, request more function
    fun requestMore(query: String, type: Int) {
        requestAndSaveData(query, type)
    }

    //  request more opn pull request from an selected repo

    fun requestMorePullRequest(githubOwnerName: String, githubRepoName: String) {
        requestAndSavePullData(githubOwnerName, githubRepoName)
    }

    private fun requestAndSaveData(query: String, type: Int?) {
        if (isRequestInProgress_SearchRepo) return

        isRequestInProgress_SearchRepo = true
        searchRepos(service, query, type!!, lastRequestedPage_SearchRepo, NETWORK_PAGE_SIZE, disposables,{ repos ->

            cache.delete {
                cache.insert(repos, {
                    lastRequestedPage_SearchRepo++
                    isRequestInProgress_SearchRepo = false
                })
            }

        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress_SearchRepo = false
        })
    }

    fun getPullRequestFromRepo(
        githubOwnerName: String, githubRepoName: String, onSuccess: (success: String) -> Unit,
        onError: (error: String) -> Unit
    ): RepoPullResult {


        lastRequestedPage_ViewPull = 1
        requestAndSavePullData(githubOwnerName, githubRepoName)

        val dataSourceFactory = cache.pullDataFromRepo()

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE).build()

        if (data != null) {
            onSuccess.invoke("Success")
        } else {
            onError.invoke("Success")

        }
        return RepoPullResult(data, networkErrors)


    }


    private fun requestAndSavePullData(githubOwnerName: String, githubRepoName: String) {

        if (isRequestInProgress_ViewPull) return

        isRequestInProgress_ViewPull = true
        searchPullFromRepo(
            service,
            githubOwnerName,
            githubRepoName,
            lastRequestedPage_SearchRepo,
            NETWORK_PAGE_SIZE,
            disposables,
            { repos ->

                cache.insertPullRepo(repos, {
                    lastRequestedPage_ViewPull++
                    isRequestInProgress_ViewPull = false
                })
            },
            { error ->
                networkErrors.postValue(error)
                isRequestInProgress_ViewPull = false
            })

    }


    companion object {
        private const val NETWORK_PAGE_SIZE = 50
        private const val DATABASE_PAGE_SIZE = 20
    }
}