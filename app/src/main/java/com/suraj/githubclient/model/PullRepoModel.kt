package com.suraj.githubclient.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "PullRepoModel")
data class PullRepoModel(
    @PrimaryKey @field:SerializedName("id") val id: Long,
    @field:SerializedName("username") var username: String,
    @field:SerializedName("useravatar") var useravatar: String,
    @field:SerializedName("userpullmessage")
    var userpullmessage: String
)
