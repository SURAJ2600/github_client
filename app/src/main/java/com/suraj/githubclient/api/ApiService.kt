package com.suraj.githubclient.api

import android.util.Log
import com.example.android.codelabs.paging.model.Repo
import com.google.gson.JsonElement
import com.suraj.githubclient.model.PullRepoModel
import com.suraj.githubclient.model.PulllRepoResponse
import com.suraj.githubclient.model.RepoSearchResponse
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.ArrayList
import java.util.concurrent.TimeUnit


private const val TAG = "GithubService"
private const val PO_QUALIFIER = "in:name,description"
private const val PRE_QUALIFIER = "user:"

/**
 * Search repos based on a query.
 * Trigger a request to the Github searchRepo API with the following params:
 * @param query searchRepo keyword
 * @param page request page index
 * @param itemsPerPage number of repositories to be returned by the Github API per page
 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of repos received
 * @param onError function that defines how to handle request failure
 */
fun searchRepos(
    service: ApiService,
    query: String,
    type: Int,
    page: Int,
    itemsPerPage: Int,
    disposables: CompositeDisposable,
    onSuccess: (repos: List<Repo>) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(TAG, "query: $query, page: $page, itemsPerPage: $itemsPerPage")


    val apiQuery = if (type == 0) {
        query + PO_QUALIFIER
    } else {
        PRE_QUALIFIER + query
    }

    service.searchRepos(apiQuery, page, itemsPerPage).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribeBy(
            onSuccess = {

                Log.d(TAG, "got a response $it")
                if (it != null) {
                    val repos = it.items ?: emptyList()
                    onSuccess(repos)
                } else {
                    onError("Unknown error")
                }
            },
            onError = {

                onError(it.message ?: "unknown error")

            }
        ).addTo(disposables)
}

/**
 * Search pull based on a owner_name and repo name.
 * Trigger a request to the Github searchRepo API with the following params:
 * @param owner_name  keyword
 *   @param repo_name  keyword
 * @param page request page index
 * @param itemsPerPage number of repositories to be returned by the Github API per page
 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of repos received
 * @param onError function that defines how to handle request failure
 */
fun searchPullFromRepo(
    service: ApiService,
    owner_name: String,
    repo_name: String,
    page: Int,
    itemsPerPage: Int,
    disposables: CompositeDisposable,
    onSuccess: (repos: List<PullRepoModel>) -> Unit,
    onError: (error: String) -> Unit
) {


    service.searchPullsFromRepo(owner_name, repo_name, "All", page, itemsPerPage).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeBy(

            onSuccess = {

                if (it != null) {

                    var PullRepolist = it as List<PulllRepoResponse>
                    var pullRepoModelList = ArrayList<PullRepoModel>()
                    for (i in 0..PullRepolist.size - 1) {
                        var pullMode = PullRepoModel(
                            PullRepolist.get(i)?.id?.toLong()!!,
                            PullRepolist.get(i)?.user?.login!!,
                            PullRepolist.get(i)?.user?.avatar_url!!,
                            "" + PullRepolist.get(i)?.body
                        )
                        pullRepoModelList.add(pullMode)

                    }
                    onSuccess(pullRepoModelList)
                } else {
                    onError("Unknown error")

                }
            },
            onError = {
                Log.d(TAG, "fail to get data")
                onError(it.message ?: "unknown error")
            }
        ).addTo(disposables)

}


interface ApiService {
    @GET("search/repositories?sort=stars")
    fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Single<RepoSearchResponse>

    @GET("repos/{owner_name}/{repo_name}/pulls")
    fun searchPullsFromRepo(
        @Path("owner_name") owner_name: String,
        @Path("repo_name") repo_name: String,
        @Query("state") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Single<List<PulllRepoResponse>>


}
