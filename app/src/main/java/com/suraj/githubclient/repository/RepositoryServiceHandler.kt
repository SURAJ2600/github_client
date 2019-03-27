package com.suraj.githubclient.repository

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.android.codelabs.paging.model.RepoSearchResult
import com.suraj.githubclient.Utilities.LogsUtils
import com.suraj.githubclient.api.ApiService
import com.suraj.githubclient.api.searchPullFromRepo
import com.suraj.githubclient.api.searchRepos
import com.suraj.githubclient.db.GithubLocalCache



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


    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage_ViewPull = 1
    // avoid triggering multiple requests in the same time
    private var isRequestInProgress_ViewPull= false

    /**
     * Search repositories whose names match the query.
     */
    fun search(query: String, type:Int): RepoSearchResult {
        Log.d("GithubRepository", "New query: $query")
        lastRequestedPage_SearchRepo = 1
        requestAndSaveData(query,type)

        // Get data from the local cache
        val data = cache.reposByName(query)

        return RepoSearchResult(data, networkErrors)
    }

    fun requestMore(query: String,type:Int) {
        requestAndSaveData(query, type)
    }

    private fun requestAndSaveData(query: String, type: Int?) {
        if (isRequestInProgress_SearchRepo) return

        isRequestInProgress_SearchRepo = true
        searchRepos(service, query,type!!, lastRequestedPage_SearchRepo, NETWORK_PAGE_SIZE, { repos ->
            LogsUtils.makeLogE("sasa",">>"+repos)
            cache.insert(repos, {
                lastRequestedPage_SearchRepo++
                isRequestInProgress_SearchRepo = false
            })
        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress_SearchRepo = false
        })
    }

     fun getPullRequestFromRepo(githubOwnerName: String, githubRepoName: String) {


        lastRequestedPage_ViewPull = 1
        requestAndSavePullData(githubOwnerName,githubRepoName)

        // Get data from the local cache
      //  val data = cache.reposByName(query)
///
    //  return RepoSearchResult(null!!, networkErrors)


    }


    private  fun requestAndSavePullData(githubOwnerName: String, githubRepoName: String)
    {

        if (isRequestInProgress_ViewPull) return

        isRequestInProgress_ViewPull = true
        searchPullFromRepo(service, githubOwnerName,githubRepoName, lastRequestedPage_SearchRepo, NETWORK_PAGE_SIZE, { repos ->
            LogsUtils.makeLogE("sasa",">>"+repos)

        }, { error ->
            networkErrors.postValue(error)
            isRequestInProgress_ViewPull = false
        })

    }


    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}