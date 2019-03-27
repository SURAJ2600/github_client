package com.suraj.githubclient.api

import com.google.gson.JsonElement
import com.suraj.githubclient.model.RepoSearchResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService
{
    @GET("search/repositories?sort=stars")
    fun searchRepos(@Query("q") query: String,
                    @Query("page") page: Int,
                    @Query("per_page") itemsPerPage: Int): Single<RepoSearchResponse>

    @GET("repos/{owner_name}/{repo_name}/pulls")
    fun searchPullsFromRepo(@Path("owner_name") owner_name:String,
                            @Path("repo_name") repo_name:String,
                            @Query("state") query: String,
                            @Query("page") page: Int,
                            @Query("per_page") itemsPerPage: Int): Single<JsonElement>


}
