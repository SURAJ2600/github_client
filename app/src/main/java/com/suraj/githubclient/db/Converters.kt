package com.suraj.githubclient.db

import android.arch.persistence.room.TypeConverter
import com.example.android.codelabs.paging.model.owner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.security.acl.Owner


/**
 * Created by suraj
 */

open class Converters {




    @TypeConverter
    fun fromstring_lift_detailarray(value: String): owner {
        val listType = object : TypeToken<owner>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromarray_buyerslift_detail(list: owner): String {
        val gson = Gson()
        return gson.toJson(list)
    }



}
