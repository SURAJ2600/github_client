package com.suraj.githubclient.model

import com.example.android.codelabs.paging.model.Repo
import com.google.gson.annotations.SerializedName

data class PulllRepoResponse(
    @SerializedName("id") val id: String?= "",
@SerializedName("user") val user: user? = null,
@SerializedName("body") val body: String = ""

)


data class user(@SerializedName("login") val login: String = "",
                @SerializedName("id") val id: String = "",
                @SerializedName("avatar_url") val avatar_url: String = ""

)
